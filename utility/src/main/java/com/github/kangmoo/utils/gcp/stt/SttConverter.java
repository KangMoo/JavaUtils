package com.github.kangmoo.utils.gcp.stt;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.*;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.protobuf.*;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.OneofDescriptor;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author kangmoo Heo
 */
public class SttConverter {
    private static final Logger log = getLogger(SttConverter.class);
    private static final int STREAMING_LIMIT = 290000; // ~5 minutes

    private final ByteArrayOutputStream inputs;
    private final ArrayList<StreamingRecognizeResponse> results = new ArrayList<>();
    private final RecognitionConfig recognitionConfig;

    private boolean isRunning = false;
    private long startTime;

    private Consumer<String> onResponse;
    private SpeechClient client;
    private ClientStream<StreamingRecognizeRequest> clientStream;
    private StreamingRecognizeRequest request;
    private StreamController referenceToStreamController;
    private ScheduledExecutorService executor;
    private ResponseObserver<StreamingRecognizeResponse> responseObserver;

    private SttConverter(Consumer<String> onResponse, RecognitionConfig recognitionConfig) {
        this.inputs = new ByteArrayOutputStream();
        this.onResponse = onResponse;

        this.recognitionConfig = recognitionConfig;
    }

    public static StreamRecognizerBuilder newBuilder() {
        return new StreamRecognizerBuilder();
    }

    public void start() {
        synchronized (this) {
            if (this.isRunning) {
                log.warn("STT_{} Already Started", this.hashCode());
                return;
            }
            this.isRunning = true;
            this.connect();
        }
        this.results.clear();
        this.executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("STT_" + this.hashCode()).build());
        executor.scheduleWithFixedDelay(this::streamRecognize, 0, 10, TimeUnit.MILLISECONDS);
        log.debug("STT_{} Started", this.hashCode());
    }

    public void stop() {
        synchronized (this) {
            if (!this.isRunning) {
                log.warn("STT_{} Already Stopped", this.hashCode());
                return;
            }
            this.isRunning = false;
            this.executor.shutdown();
            this.disconnect();
        }
        log.debug("STT_{} Stopped", this.hashCode());
    }

    public void inputData(byte[] data) {
        synchronized (inputs) {
            try {
                inputs.write(data);
            } catch (IOException e) {
                log.warn("Err Occurs while write data", e);
            }
        }
    }

    private void connect() {
        try {
            this.responseObserver = new ResponseObserver<>() {
                public void onStart(StreamController controller) {
                    referenceToStreamController = controller;
                }

                public void onResponse(StreamingRecognizeResponse response) {
                    synchronized (results) {
                        results.add(response);
                        onResponse.accept(response.getResultsList().get(0).getAlternativesList().get(0).getTranscript());
                    }
                }

                public void onComplete() {
                }

                public void onError(Throwable t) {
                    log.error("Err occurs on ResponseObserver", t);
                }
            };
            this.client = SpeechClient.create();
            this.clientStream = client.streamingRecognizeCallable().splitCall(responseObserver);
            this.request = StreamingRecognizeRequest.newBuilder().setStreamingConfig(StreamingRecognitionConfig.newBuilder().setConfig(recognitionConfig).setInterimResults(true).build()).build();
            this.clientStream.send(request);
            this.startTime = System.currentTimeMillis();
            log.debug("Connect Success");
        } catch (IOException e) {
            this.client.close();
            log.debug("Connect Fail", e);
        }
    }

    private void disconnect() {
        try {
            this.responseObserver.onComplete();
            this.client.close();
            log.debug("Disconnect Success");
        } catch (Exception e) {
            log.debug("Disconnect Fail", e);
        }

    }

    private void streamRecognize() {
        if (!isRunning) return;
        if (System.currentTimeMillis() - startTime < STREAMING_LIMIT) {
            byte[] data;
            synchronized (inputs) {
                data = inputs.toByteArray();
                if (data.length == 0) return;
                inputs.reset();
            }
            request = StreamingRecognizeRequest.newBuilder().setAudioContent(ByteString.copyFrom(data)).build();
            clientStream.send(request);
        } else {
            this.responseObserver.onComplete();
            log.debug("Restarting Request");
            synchronized (this) {
                if (isRunning) {
                    this.disconnect();
                    this.connect();
                }
            }
        }
    }

    public List<StreamingRecognizeResponse> getResults() {
        synchronized (results) {
            return new ArrayList<>(results);
        }
    }

    public List<String> getResultTexts() {
        synchronized (results) {
            return results.stream().map(o -> o.getResultsList().get(0).getAlternativesList().get(0).getTranscript()).collect(Collectors.toList());
        }
    }

    public static final class StreamRecognizerBuilder {
        private Consumer<String> onResponse = o -> {
        };
        private final RecognitionConfig.Builder recognitionConfigBuilder = RecognitionConfig.newBuilder().setEncoding(AudioEncoding.LINEAR16).setSampleRateHertz(16000).setLanguageCode("ko-KR");

        private StreamRecognizerBuilder() {
        }

        public StreamRecognizerBuilder setOnResponse(Consumer<String> onResponse) {
            this.onResponse = onResponse;
            return this;
        }

        public StreamRecognizerBuilder clear() {
            recognitionConfigBuilder.clear();
            return this;
        }

        public StreamRecognizerBuilder setField(FieldDescriptor field, Object value) {
            recognitionConfigBuilder.setField(field, value);
            return this;
        }

        public StreamRecognizerBuilder clearField(FieldDescriptor field) {
            recognitionConfigBuilder.clearField(field);
            return this;
        }

        public StreamRecognizerBuilder clearOneof(OneofDescriptor oneof) {
            recognitionConfigBuilder.clearOneof(oneof);
            return this;
        }

        public StreamRecognizerBuilder setRepeatedField(FieldDescriptor field, int index, Object value) {
            recognitionConfigBuilder.setRepeatedField(field, index, value);
            return this;
        }

        public StreamRecognizerBuilder addRepeatedField(FieldDescriptor field, Object value) {
            recognitionConfigBuilder.addRepeatedField(field, value);
            return this;
        }

        public StreamRecognizerBuilder mergeFrom(Message other) {
            recognitionConfigBuilder.mergeFrom(other);
            return this;
        }

        public StreamRecognizerBuilder mergeFrom(RecognitionConfig other) {
            recognitionConfigBuilder.mergeFrom(other);
            return this;
        }

        public StreamRecognizerBuilder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            recognitionConfigBuilder.mergeFrom(input, extensionRegistry);
            return this;
        }

        public StreamRecognizerBuilder setEncodingValue(int value) {
            recognitionConfigBuilder.setEncodingValue(value);
            return this;
        }

        public StreamRecognizerBuilder setEncoding(AudioEncoding value) {
            recognitionConfigBuilder.setEncoding(value);
            return this;
        }

        public StreamRecognizerBuilder clearEncoding() {
            recognitionConfigBuilder.clearEncoding();
            return this;
        }

        public StreamRecognizerBuilder setSampleRateHertz(int value) {
            recognitionConfigBuilder.setSampleRateHertz(value);
            return this;
        }

        public StreamRecognizerBuilder clearSampleRateHertz() {
            recognitionConfigBuilder.clearSampleRateHertz();
            return this;
        }

        public StreamRecognizerBuilder setAudioChannelCount(int value) {
            recognitionConfigBuilder.setAudioChannelCount(value);
            return this;
        }

        public StreamRecognizerBuilder clearAudioChannelCount() {
            recognitionConfigBuilder.clearAudioChannelCount();
            return this;
        }

        public StreamRecognizerBuilder setEnableSeparateRecognitionPerChannel(boolean value) {
            recognitionConfigBuilder.setEnableSeparateRecognitionPerChannel(value);
            return this;
        }

        public StreamRecognizerBuilder clearEnableSeparateRecognitionPerChannel() {
            recognitionConfigBuilder.clearEnableSeparateRecognitionPerChannel();
            return this;
        }

        public StreamRecognizerBuilder setLanguageCode(String value) {
            recognitionConfigBuilder.setLanguageCode(value);
            return this;
        }

        public StreamRecognizerBuilder clearLanguageCode() {
            recognitionConfigBuilder.clearLanguageCode();
            return this;
        }

        public StreamRecognizerBuilder setLanguageCodeBytes(ByteString value) {
            recognitionConfigBuilder.setLanguageCodeBytes(value);
            return this;
        }

        public StreamRecognizerBuilder setAlternativeLanguageCodes(int index, String value) {
            recognitionConfigBuilder.setAlternativeLanguageCodes(index, value);
            return this;
        }

        public StreamRecognizerBuilder addAlternativeLanguageCodes(String value) {
            recognitionConfigBuilder.addAlternativeLanguageCodes(value);
            return this;
        }

        public StreamRecognizerBuilder addAllAlternativeLanguageCodes(Iterable<String> values) {
            recognitionConfigBuilder.addAllAlternativeLanguageCodes(values);
            return this;
        }

        public StreamRecognizerBuilder clearAlternativeLanguageCodes() {
            recognitionConfigBuilder.clearAlternativeLanguageCodes();
            return this;
        }

        public StreamRecognizerBuilder addAlternativeLanguageCodesBytes(ByteString value) {
            recognitionConfigBuilder.addAlternativeLanguageCodesBytes(value);
            return this;
        }

        public StreamRecognizerBuilder setMaxAlternatives(int value) {
            recognitionConfigBuilder.setMaxAlternatives(value);
            return this;
        }

        public StreamRecognizerBuilder clearMaxAlternatives() {
            recognitionConfigBuilder.clearMaxAlternatives();
            return this;
        }

        public StreamRecognizerBuilder setProfanityFilter(boolean value) {
            recognitionConfigBuilder.setProfanityFilter(value);
            return this;
        }

        public StreamRecognizerBuilder clearProfanityFilter() {
            recognitionConfigBuilder.clearProfanityFilter();
            return this;
        }

        public StreamRecognizerBuilder setAdaptation(SpeechAdaptation value) {
            recognitionConfigBuilder.setAdaptation(value);
            return this;
        }

        public StreamRecognizerBuilder setAdaptation(SpeechAdaptation.Builder builderForValue) {
            recognitionConfigBuilder.setAdaptation(builderForValue);
            return this;
        }

        public StreamRecognizerBuilder mergeAdaptation(SpeechAdaptation value) {
            recognitionConfigBuilder.mergeAdaptation(value);
            return this;
        }

        public StreamRecognizerBuilder clearAdaptation() {
            recognitionConfigBuilder.clearAdaptation();
            return this;
        }

        public StreamRecognizerBuilder setSpeechContexts(int index, SpeechContext value) {
            recognitionConfigBuilder.setSpeechContexts(index, value);
            return this;
        }

        public StreamRecognizerBuilder setSpeechContexts(int index, SpeechContext.Builder builderForValue) {
            recognitionConfigBuilder.setSpeechContexts(index, builderForValue);
            return this;
        }

        public StreamRecognizerBuilder addSpeechContexts(SpeechContext value) {
            recognitionConfigBuilder.addSpeechContexts(value);
            return this;
        }

        public StreamRecognizerBuilder addSpeechContexts(int index, SpeechContext value) {
            recognitionConfigBuilder.addSpeechContexts(index, value);
            return this;
        }

        public StreamRecognizerBuilder addSpeechContexts(SpeechContext.Builder builderForValue) {
            recognitionConfigBuilder.addSpeechContexts(builderForValue);
            return this;
        }

        public StreamRecognizerBuilder addSpeechContexts(int index, SpeechContext.Builder builderForValue) {
            recognitionConfigBuilder.addSpeechContexts(index, builderForValue);
            return this;
        }

        public StreamRecognizerBuilder addAllSpeechContexts(Iterable<? extends SpeechContext> values) {
            recognitionConfigBuilder.addAllSpeechContexts(values);
            return this;
        }

        public StreamRecognizerBuilder clearSpeechContexts() {
            recognitionConfigBuilder.clearSpeechContexts();
            return this;
        }

        public StreamRecognizerBuilder removeSpeechContexts(int index) {
            recognitionConfigBuilder.removeSpeechContexts(index);
            return this;
        }

        public StreamRecognizerBuilder setEnableWordTimeOffsets(boolean value) {
            recognitionConfigBuilder.setEnableWordTimeOffsets(value);
            return this;
        }

        public StreamRecognizerBuilder clearEnableWordTimeOffsets() {
            recognitionConfigBuilder.clearEnableWordTimeOffsets();
            return this;
        }

        public StreamRecognizerBuilder setEnableWordConfidence(boolean value) {
            recognitionConfigBuilder.setEnableWordConfidence(value);
            return this;
        }

        public StreamRecognizerBuilder clearEnableWordConfidence() {
            recognitionConfigBuilder.clearEnableWordConfidence();
            return this;
        }

        public StreamRecognizerBuilder setEnableAutomaticPunctuation(boolean value) {
            recognitionConfigBuilder.setEnableAutomaticPunctuation(value);
            return this;
        }

        public StreamRecognizerBuilder clearEnableAutomaticPunctuation() {
            recognitionConfigBuilder.clearEnableAutomaticPunctuation();
            return this;
        }

        public StreamRecognizerBuilder setEnableSpokenPunctuation(BoolValue value) {
            recognitionConfigBuilder.setEnableSpokenPunctuation(value);
            return this;
        }

        public StreamRecognizerBuilder setEnableSpokenPunctuation(BoolValue.Builder builderForValue) {
            recognitionConfigBuilder.setEnableSpokenPunctuation(builderForValue);
            return this;
        }

        public StreamRecognizerBuilder mergeEnableSpokenPunctuation(BoolValue value) {
            recognitionConfigBuilder.mergeEnableSpokenPunctuation(value);
            return this;
        }

        public StreamRecognizerBuilder clearEnableSpokenPunctuation() {
            recognitionConfigBuilder.clearEnableSpokenPunctuation();
            return this;
        }

        public StreamRecognizerBuilder setEnableSpokenEmojis(BoolValue value) {
            recognitionConfigBuilder.setEnableSpokenEmojis(value);
            return this;
        }

        public StreamRecognizerBuilder setEnableSpokenEmojis(BoolValue.Builder builderForValue) {
            recognitionConfigBuilder.setEnableSpokenEmojis(builderForValue);
            return this;
        }

        public StreamRecognizerBuilder mergeEnableSpokenEmojis(BoolValue value) {
            recognitionConfigBuilder.mergeEnableSpokenEmojis(value);
            return this;
        }

        public StreamRecognizerBuilder clearEnableSpokenEmojis() {
            recognitionConfigBuilder.clearEnableSpokenEmojis();
            return this;
        }

        public StreamRecognizerBuilder setDiarizationConfig(SpeakerDiarizationConfig value) {
            recognitionConfigBuilder.setDiarizationConfig(value);
            return this;
        }

        public StreamRecognizerBuilder setDiarizationConfig(SpeakerDiarizationConfig.Builder builderForValue) {
            recognitionConfigBuilder.setDiarizationConfig(builderForValue);
            return this;
        }

        public StreamRecognizerBuilder mergeDiarizationConfig(SpeakerDiarizationConfig value) {
            recognitionConfigBuilder.mergeDiarizationConfig(value);
            return this;
        }

        public StreamRecognizerBuilder clearDiarizationConfig() {
            recognitionConfigBuilder.clearDiarizationConfig();
            return this;
        }

        public StreamRecognizerBuilder setMetadata(RecognitionMetadata value) {
            recognitionConfigBuilder.setMetadata(value);
            return this;
        }

        public StreamRecognizerBuilder setMetadata(RecognitionMetadata.Builder builderForValue) {
            recognitionConfigBuilder.setMetadata(builderForValue);
            return this;
        }

        public StreamRecognizerBuilder mergeMetadata(RecognitionMetadata value) {
            recognitionConfigBuilder.mergeMetadata(value);
            return this;
        }

        public StreamRecognizerBuilder clearMetadata() {
            recognitionConfigBuilder.clearMetadata();
            return this;
        }

        public StreamRecognizerBuilder setModel(String value) {
            recognitionConfigBuilder.setModel(value);
            return this;
        }

        public StreamRecognizerBuilder clearModel() {
            recognitionConfigBuilder.clearModel();
            return this;
        }

        public StreamRecognizerBuilder setModelBytes(ByteString value) {
            recognitionConfigBuilder.setModelBytes(value);
            return this;
        }

        public StreamRecognizerBuilder clearUseEnhanced() {
            recognitionConfigBuilder.clearUseEnhanced();
            return this;
        }

        public StreamRecognizerBuilder setUnknownFields(UnknownFieldSet unknownFields) {
            recognitionConfigBuilder.setUnknownFields(unknownFields);
            return this;
        }

        public StreamRecognizerBuilder mergeUnknownFields(UnknownFieldSet unknownFields) {
            recognitionConfigBuilder.mergeUnknownFields(unknownFields);
            return this;
        }

        public StreamRecognizerBuilder mergeFrom(CodedInputStream input) throws IOException {
            recognitionConfigBuilder.mergeFrom(input);
            return this;
        }

        public StreamRecognizerBuilder mergeFrom(ByteString data) throws InvalidProtocolBufferException {
            recognitionConfigBuilder.mergeFrom(data);
            return this;
        }

        public StreamRecognizerBuilder mergeFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            recognitionConfigBuilder.mergeFrom(data, extensionRegistry);
            return this;
        }

        public StreamRecognizerBuilder mergeFrom(byte[] data) throws InvalidProtocolBufferException {
            recognitionConfigBuilder.mergeFrom(data);
            return this;
        }

        public StreamRecognizerBuilder mergeFrom(byte[] data, int off, int len) throws InvalidProtocolBufferException {
            recognitionConfigBuilder.mergeFrom(data, off, len);
            return this;
        }

        public StreamRecognizerBuilder mergeFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            recognitionConfigBuilder.mergeFrom(data, extensionRegistry);
            return this;
        }

        public StreamRecognizerBuilder mergeFrom(byte[] data, int off, int len, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            recognitionConfigBuilder.mergeFrom(data, off, len, extensionRegistry);
            return this;
        }

        public StreamRecognizerBuilder mergeFrom(InputStream input) throws IOException {
            recognitionConfigBuilder.mergeFrom(input);
            return this;
        }

        public StreamRecognizerBuilder mergeFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            recognitionConfigBuilder.mergeFrom(input, extensionRegistry);
            return this;
        }

        public SttConverter build() {
            return new SttConverter(onResponse, recognitionConfigBuilder.build());
        }
    }
}
