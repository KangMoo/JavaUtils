package com.github.kangmoo.utils.audio.pcm;

import java.util.Arrays;

public class PcmResampler {
    int inSampleRate;                   // 입력 샘플 레이트
    int outSampleRate;                  // 출력 샘플 레이트
    short numChannels;                  // 채널 수
    short bitsPerSample;                // 샘플당 비트 수
    private byte[] lastFrameProcessed;  // 마지막으로 처리된 프레임
    private int decimationOffset;       // 감소 오프셋

    public PcmResampler(int inSampleRate, int outSampleRate) {
        this(inSampleRate, outSampleRate, (short) 1, (short) 16);
    }

    private PcmResampler(int inSampleRate, int outSampleRate, short numChannels, short bitsPerSample) {
        // 입력 값 검증
        if (bitsPerSample <= 0 || bitsPerSample > 16) {
            throw new IllegalArgumentException("The value of bitsPerSample is incorrect.");
        }
        if (outSampleRate <= 0) {
            throw new IllegalArgumentException("The value of outSampleRate is incorrect.");
        }
        this.inSampleRate = inSampleRate;
        this.outSampleRate = outSampleRate;
        this.numChannels = numChannels;
        this.bitsPerSample = bitsPerSample;
    }

    public byte[] process(byte[] input) {
        // 샘플레이트가 같거나 입력 데이터가 없는 경우 처리 필요 없으므로 바로 반환
        if (outSampleRate == inSampleRate || input.length == 0) {
            return input;
        }

        // 각 프레임의 바이트 크기 및 샘플레이트의 최소공배수 계산
        int frameSize = (this.bitsPerSample / 8) * this.numChannels;
        int sampleRateLCM = leastCommonMultiple(inSampleRate, outSampleRate);

        // 패딩을 위한 바이트 수 계산
        int padding = ((sampleRateLCM / inSampleRate) - 1) * frameSize;

        // 패딩된 데이터를 위한 배열 생성
        // 이전에 처리된 프레임이 있으면 그 프레임을 기반으로 패딩
        int baseLength = input.length + (input.length / frameSize * padding);
        byte[] stuffedInput = new byte[lastFrameProcessed == null ? baseLength - padding : baseLength];
        int stuffedInputPointer = lastFrameProcessed == null ? 0 : padding;

        // 입력 데이터에 패딩 추가
        for (int i = 0; i < input.length; i += frameSize) {
            byte[] frame = Arrays.copyOfRange(input, i, i + frameSize);

            for (byte b : frame) {
                stuffedInput[stuffedInputPointer] = b;
                stuffedInputPointer++;
            }

            stuffedInputPointer += padding;
        }

        // 선형 보간을 통해 패딩된 데이터의 값을 결정
        for (int i = -(lastFrameProcessed == null ? 0 : frameSize); i < stuffedInput.length; i += frameSize + padding) {
            int j = i + frameSize + padding;

            // 보간할 두 프레임 추출
            byte[] leftFrame = i >= 0 ? Arrays.copyOfRange(stuffedInput, i, i + frameSize) : lastFrameProcessed;
            byte[] rightFrame = j >= stuffedInput.length ? null : Arrays.copyOfRange(stuffedInput, j, j + frameSize);

            for (int channel = 0; channel < this.numChannels; channel++) {
                int leftSample = this.bitsPerSample > 8 ? getIntFromBytes(Arrays.copyOfRange(leftFrame, channel * (this.bitsPerSample / 8), (channel + 1) * (this.bitsPerSample / 8))) : getUnsignedIntFromByte(leftFrame[channel]);
                int rightSample;

                // 오른쪽 프레임이 null이면 0을 사용
                if (rightFrame == null) {
                    rightSample = (byte) 0;
                } else {
                    if (this.bitsPerSample > 8) {
                        int start = channel * (this.bitsPerSample / 8);
                        int end = (channel + 1) * (this.bitsPerSample / 8);
                        byte[] subArray = new byte[end - start];

                        System.arraycopy(rightFrame, start, subArray, 0, subArray.length);

                        rightSample = getIntFromBytes(subArray);
                    } else {
                        rightSample = getUnsignedIntFromByte(rightFrame[channel]);
                    }
                }

                // 선형 보간 수행
                for (int k = 0; k < (padding / frameSize); k++) {
                    int index = i + (frameSize * (k + 1)) + (channel * (this.bitsPerSample / 8));

                    if (index + (this.bitsPerSample / 8) <= stuffedInput.length) {
                        int interpolatedSample = interpolateLinear(leftSample, rightSample,
                                (k + 1) / (((double) padding / frameSize) + 1));

                        byte[] interpolatedBytes;

                        if (this.bitsPerSample == 8) {
                            interpolatedBytes = new byte[]{(byte) interpolatedSample};
                        } else if (this.bitsPerSample == 16) {
                            interpolatedBytes = getShortBytes((short) interpolatedSample);
                        } else {
                            interpolatedBytes = getIntBytes(interpolatedSample);
                        }

                        System.arraycopy(interpolatedBytes, 0, stuffedInput, index, interpolatedBytes.length);
                    }
                }
            }
        }

        // 샘플 감소 (Decimation)
        int decimationRate = sampleRateLCM / outSampleRate;
        int outputSize = (int) Math.round(((double) stuffedInput.length / decimationRate));

        // 출력 파일의 끝이 일정하지 않으면 추가 바이트 삽입
        while (outputSize % frameSize != 0) {
            outputSize++;
        }

        byte[] reSampledInput = new byte[outputSize];
        int decimatedInputPointer = 0;

        // 샘플 감소를 통한 재샘플링
        for (int i = decimationOffset * frameSize; i < stuffedInput.length; i += decimationRate * frameSize) {
            byte[] frame = Arrays.copyOfRange(stuffedInput, i, i + frameSize);

            for (byte b : frame) {
                reSampledInput[decimatedInputPointer] = b;
                decimatedInputPointer++;
            }

            decimationOffset = Math.abs(stuffedInput.length - (decimationRate * frameSize) - i) / frameSize;
        }

        // 남은 바이트에 silence 값 (0) 채우기
        while (decimatedInputPointer < outputSize) {
            reSampledInput[decimatedInputPointer] = (byte) (0);
            decimatedInputPointer++;
        }

        // 처리된 마지막 프레임을 저장하여 다음 호출에 사용
        lastFrameProcessed = Arrays.copyOfRange(input, input.length - frameSize, input.length);

        return reSampledInput;
    }

    private static int leastCommonMultiple(int a, int b) {
        return a / greatestCommonDivisor(a, b) * b;
    }

    private static int greatestCommonDivisor(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }

    private static int interpolateLinear(int a, int b, double distance) {
        if (distance <= 0) {
            return a;
        } else if (distance >= 1) {
            return b;
        }

        return (int) Math.round(a + ((b - a) * distance));
    }

    private static int getIntFromBytes(byte[] data) {
        if (data == null || data.length == 0) {
            return 0;
        }

        return switch (data.length) {
            case 1 -> data[0];
            case 2 -> (data[1] << 8) | (data[0] & 0xff);
            case 3 -> (data[2] << 16) | ((data[1] & 0xff) << 8) | (data[0] & 0xff);
            case 4 -> (data[3] << 24) | ((data[2] & 0xff) << 16) | ((data[1] & 0xff) << 8) | (data[0] & 0xff);
            default -> throw new IllegalArgumentException();
        };
    }

    private static int getUnsignedIntFromByte(byte data) {
        return data & 0xff;
    }

    private static byte[] getIntBytes(int data) {
        return new byte[]{
                (byte) (data),
                (byte) (data >> 8),
                (byte) (data >> 16),
                (byte) (data >> 24)
        };
    }

    private static byte[] getShortBytes(short data) {
        return new byte[]{
                (byte) (data),
                (byte) (data >> 8)
        };
    }
}
