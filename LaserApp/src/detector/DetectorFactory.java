package detector;

// don't expose implementation of Detector; just provide factory and interface
public class DetectorFactory {
	/** Return instance of Detector, on which to run 'match' */
	public static IDetector getDetector(){
		// these details shouldn't matter to whoever is using the class
		//if(Speaker.getDetector()){System.out.println("DetectorFactory.getDetector: Instantiating DefaultDetector");}
		return new DefaultDetector();
//		return new SimpleDetector();
	}
	
	public static IDetector getDetector(int nPlayers){
		return new BankDetector(nPlayers);
	}

}