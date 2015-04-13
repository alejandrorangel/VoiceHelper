package mx.edu.cicese.alejandro.audio.processing;

public class PitchAnalyzer {
	private final double THRESHOLD = 0.35;
	
	public PitchAnalyzer(double[] frame, int sampleRate, int bufferSize){
		_samplingFreq = sampleRate;
		_frameSize = bufferSize;
		_frameSignal = frame;
		_yinBuffer = new double[_frameSize /2];
	}
	
	public double featureExtraction(){
		int tauEstimate = -1;
		double pitchInHertz;
		pitchInHertz=-1;

		difference();

		//step 3
		cumulativeMeanNormalizedDifference();

		//step 4
		tauEstimate = absoluteThreshold();

		//step 5
		if(tauEstimate != -1){
			 double betterTau = parabolicInterpolation(tauEstimate);

			//conversion to Hz
			pitchInHertz = _samplingFreq /betterTau;
		}

		return pitchInHertz;
	}
	

	/**
	 * Implements the difference function as described
	 * in step 2 of the YIN paper
	 */
	private void difference(){
		int j,tau;
		double delta;
		for(tau=0;tau < _yinBuffer.length;tau++){
			_yinBuffer[tau] = 0;
		}
		for(tau = 1 ; tau < _yinBuffer.length ; tau++){
			for(j = 0 ; j < _yinBuffer.length ; j++){
				delta = _frameSignal[j] - _frameSignal[j+tau];
				_yinBuffer[tau] += delta * delta;
			}
		}
	}

	/**
	 * The cumulative mean normalized difference function
	 * as described in step 3 of the YIN paper
	 * <br><code>
	 * _yinBuffer[0] == _yinBuffer[1] = 1
	 * </code>
	 *
	 */
	private void cumulativeMeanNormalizedDifference(){
		int tau;
		_yinBuffer[0] = 1;
		//Very small optimization in comparison with AUBIO
		//start the running sum with the correct value:
		//the first value of the _yinBuffer
		double runningSum = _yinBuffer[1];
		//_yinBuffer[1] is always 1
		_yinBuffer[1] = 1;
		//now start at tau = 2
		for(tau = 2 ; tau < _yinBuffer.length ; tau++){
			runningSum += _yinBuffer[tau];
			_yinBuffer[tau] *= tau / runningSum;
		}
	}

	/**
	 * Implements step 4 of the YIN paper
	 */
	private int absoluteThreshold(){
		//Uses another loop construct
		//than the AUBIO implementation
		for(int tau = 1;tau< _yinBuffer.length;tau++){
			if(_yinBuffer[tau] < THRESHOLD){
				while(tau+1 < _yinBuffer.length &&
						_yinBuffer[tau+1] < _yinBuffer[tau])
					tau++;
				return tau;
			}
		}
		//no pitch found
		return -1;
	}

	/**
	 * Implements step 5 of the YIN paper. It refines the estimated tau value
	 * using parabolic interpolation. This is needed to detect higher
	 * frequencies more precisely.
	 *
	 * @param tauEstimate
	 *            the estimated tau value.
	 * @return a better, more precise tau value.
	 */
	private double parabolicInterpolation(int tauEstimate) {
		double s0, s1, s2;
		int x0 = (tauEstimate < 1) ? tauEstimate : tauEstimate - 1;
		int x2 = (tauEstimate + 1 < _yinBuffer.length) ? tauEstimate + 1 : tauEstimate;
		if (x0 == tauEstimate)
			return (_yinBuffer[tauEstimate] <= _yinBuffer[x2]) ? tauEstimate : x2;
		if (x2 == tauEstimate)
			return (_yinBuffer[tauEstimate] <= _yinBuffer[x0]) ? tauEstimate : x0;
		s0 = _yinBuffer[x0];
		s1 = _yinBuffer[tauEstimate];
		s2 = _yinBuffer[x2];

		return tauEstimate + 0.5f * (s2 - s0 ) / (2.0f * s1 - s2 - s0);
	}

    private int _samplingFreq;
    private int _frameSize;
    private double[] _frameSignal;
    private double[] _yinBuffer;
}
