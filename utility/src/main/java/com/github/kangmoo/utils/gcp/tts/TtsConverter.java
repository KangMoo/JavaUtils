package com.github.kangmoo.utils.gcp.tts;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.*;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.OneofDescriptor;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class TtsConverter {
    private static final Logger log = getLogger(TtsConverter.class);
    private VoiceSelectionParams voice;
    private AudioConfig audioConfig;

    private TtsConverter(VoiceSelectionParams voice, AudioConfig audioConfig) {
        this.voice = voice;
        this.audioConfig = audioConfig;
    }

    public static TtsRecognizerBuilder newBuilder() {
        return new TtsRecognizerBuilder();
    }

    public ByteString convertText(String msg) {
        return convert(SynthesisInput.newBuilder().setText(msg).build());
    }

    public ByteString convertSsml(String ssml) {
        return convert(SynthesisInput.newBuilder().setSsml(ssml).build());
    }

    public ByteString convert(SynthesisInput input) {
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
            return response.getAudioContent();
        } catch (IOException e) {
            log.warn("Err Occurs while convertText", e);
            return null;
        }
    }

    public static final class TtsRecognizerBuilder {
        private final VoiceSelectionParams.Builder voiceSelectionParamsBuilder;
        private final AudioConfig.Builder audioConfigBuilder;

        private TtsRecognizerBuilder() {
            voiceSelectionParamsBuilder = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("ko-KR")
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL);
            audioConfigBuilder = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3);
        }

        public TtsRecognizerBuilder clear() {
            voiceSelectionParamsBuilder.clear();
            return this;
        }

        public TtsRecognizerBuilder setField(FieldDescriptor field, Object value) {
            voiceSelectionParamsBuilder.setField(field, value);
            return this;
        }

        public TtsRecognizerBuilder clearField(FieldDescriptor field) {
            voiceSelectionParamsBuilder.clearField(field);
            return this;
        }

        public TtsRecognizerBuilder clearOneof(OneofDescriptor oneof) {
            voiceSelectionParamsBuilder.clearOneof(oneof);
            return this;
        }

        public TtsRecognizerBuilder setRepeatedField(FieldDescriptor field, int index, Object value) {
            voiceSelectionParamsBuilder.setRepeatedField(field, index, value);
            return this;
        }

        public TtsRecognizerBuilder addRepeatedField(FieldDescriptor field, Object value) {
            voiceSelectionParamsBuilder.addRepeatedField(field, value);
            return this;
        }

        public TtsRecognizerBuilder mergeFrom(Message other) {
            voiceSelectionParamsBuilder.mergeFrom(other);
            return this;
        }

        public TtsRecognizerBuilder mergeFrom(AudioConfig other) {
            audioConfigBuilder.mergeFrom(other);
            return this;
        }

        public TtsRecognizerBuilder mergeFrom(VoiceSelectionParams other) {
            voiceSelectionParamsBuilder.mergeFrom(other);
            return this;
        }

        public TtsRecognizerBuilder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            voiceSelectionParamsBuilder.mergeFrom(input, extensionRegistry);
            return this;
        }

        public TtsRecognizerBuilder setAudioEncodingValue(int value) {
            audioConfigBuilder.setAudioEncodingValue(value);
            return this;
        }

        public TtsRecognizerBuilder setAudioEncoding(AudioEncoding value) {
            audioConfigBuilder.setAudioEncoding(value);
            return this;
        }

        public TtsRecognizerBuilder clearAudioEncoding() {
            audioConfigBuilder.clearAudioEncoding();
            return this;
        }

        public TtsRecognizerBuilder setSpeakingRate(double value) {
            audioConfigBuilder.setSpeakingRate(value);
            return this;
        }

        public TtsRecognizerBuilder clearSpeakingRate() {
            audioConfigBuilder.clearSpeakingRate();
            return this;
        }

        public TtsRecognizerBuilder setPitch(double value) {
            audioConfigBuilder.setPitch(value);
            return this;
        }

        public TtsRecognizerBuilder clearPitch() {
            audioConfigBuilder.clearPitch();
            return this;
        }

        public TtsRecognizerBuilder setVolumeGainDb(double value) {
            audioConfigBuilder.setVolumeGainDb(value);
            return this;
        }

        public TtsRecognizerBuilder clearVolumeGainDb() {
            audioConfigBuilder.clearVolumeGainDb();
            return this;
        }

        public TtsRecognizerBuilder setSampleRateHertz(int value) {
            audioConfigBuilder.setSampleRateHertz(value);
            return this;
        }

        public TtsRecognizerBuilder clearSampleRateHertz() {
            audioConfigBuilder.clearSampleRateHertz();
            return this;
        }

        public TtsRecognizerBuilder setEffectsProfileId(int index, String value) {
            audioConfigBuilder.setEffectsProfileId(index, value);
            return this;
        }

        public TtsRecognizerBuilder addEffectsProfileId(String value) {
            audioConfigBuilder.addEffectsProfileId(value);
            return this;
        }

        public TtsRecognizerBuilder addAllEffectsProfileId(Iterable<String> values) {
            audioConfigBuilder.addAllEffectsProfileId(values);
            return this;
        }

        public TtsRecognizerBuilder clearEffectsProfileId() {
            audioConfigBuilder.clearEffectsProfileId();
            return this;
        }

        public TtsRecognizerBuilder addEffectsProfileIdBytes(ByteString value) {
            audioConfigBuilder.addEffectsProfileIdBytes(value);
            return this;
        }

        public TtsRecognizerBuilder setLanguageCode(String value) {
            voiceSelectionParamsBuilder.setLanguageCode(value);
            return this;
        }

        public TtsRecognizerBuilder clearLanguageCode() {
            voiceSelectionParamsBuilder.clearLanguageCode();
            return this;
        }

        public TtsRecognizerBuilder setLanguageCodeBytes(ByteString value) {
            voiceSelectionParamsBuilder.setLanguageCodeBytes(value);
            return this;
        }

        public TtsRecognizerBuilder setName(String value) {
            voiceSelectionParamsBuilder.setName(value);
            return this;
        }

        public TtsRecognizerBuilder clearName() {
            voiceSelectionParamsBuilder.clearName();
            return this;
        }

        public TtsRecognizerBuilder setNameBytes(ByteString value) {
            voiceSelectionParamsBuilder.setNameBytes(value);
            return this;
        }

        public TtsRecognizerBuilder setSsmlGenderValue(int value) {
            voiceSelectionParamsBuilder.setSsmlGenderValue(value);
            return this;
        }

        public TtsRecognizerBuilder setSsmlGender(SsmlVoiceGender value) {
            voiceSelectionParamsBuilder.setSsmlGender(value);
            return this;
        }

        public TtsRecognizerBuilder clearSsmlGender() {
            voiceSelectionParamsBuilder.clearSsmlGender();
            return this;
        }

        public TtsRecognizerBuilder setCustomVoice(CustomVoiceParams value) {
            voiceSelectionParamsBuilder.setCustomVoice(value);
            return this;
        }

        public TtsRecognizerBuilder setCustomVoice(CustomVoiceParams.Builder builderForValue) {
            voiceSelectionParamsBuilder.setCustomVoice(builderForValue);
            return this;
        }

        public TtsRecognizerBuilder mergeCustomVoice(CustomVoiceParams value) {
            voiceSelectionParamsBuilder.mergeCustomVoice(value);
            return this;
        }

        public TtsRecognizerBuilder clearCustomVoice() {
            voiceSelectionParamsBuilder.clearCustomVoice();
            return this;
        }

        public TtsRecognizerBuilder setUnknownFields(UnknownFieldSet unknownFields) {
            voiceSelectionParamsBuilder.setUnknownFields(unknownFields);
            return this;
        }

        public TtsRecognizerBuilder mergeUnknownFields(UnknownFieldSet unknownFields) {
            voiceSelectionParamsBuilder.mergeUnknownFields(unknownFields);
            return this;
        }

        public TtsConverter build() {
            return new TtsConverter(voiceSelectionParamsBuilder.build(), audioConfigBuilder.build());
        }
    }
}
