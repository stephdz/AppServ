package org.dz.launcher;

import java.util.List;

import org.apache.log4j.Logger;

public abstract class AbstractTask implements Task {
	
	private static final Logger LOGGER = Logger.getLogger(AbstractTask.class);

	private static final int LINE_LENGTH = 80;
	private static final String SEPARATOR = "-";
	private static final String LINE_SEPARATOR = initLineSeparator();
	private static String initLineSeparator() {
		StringBuffer buf = new StringBuffer(LINE_LENGTH);
		for ( int i = 0; i < LINE_LENGTH; i++ ) {
			buf.append(SEPARATOR);
		}
		return buf.toString();
	}
	
	private String currentStep;

	@Override
	public abstract void run(List<String> args) throws TaskException;

	public void startStep(String stepName) throws TaskException {
		if ( currentStep == null ) {
			currentStep = stepName;
			
			LOGGER.info(LINE_SEPARATOR);
			
			StringBuffer buf = new StringBuffer(LINE_LENGTH);
			buf.append(SEPARATOR+SEPARATOR+" ");
			int nbSpaces = Math.max ( LINE_LENGTH - 6 - stepName.length(), 0 ) / 2;
			for ( int i = 0; i < nbSpaces; i++ ) {
				buf.append(" ");
			}
			buf.append(stepName);
			nbSpaces = Math.max ( LINE_LENGTH - 6 - stepName.length() - nbSpaces, 0 );
			for ( int i = 0; i < nbSpaces; i++ ) {
				buf.append(" ");
			}
			buf.append(" "+SEPARATOR+SEPARATOR);
			LOGGER.info(buf.toString());
			
			LOGGER.info(LINE_SEPARATOR);
		} else {
			throw new TaskException("Step \""+currentStep+"\" must be ended before \""+stepName+"\" starts");
		}
	}
	
	public void endStep() throws TaskException {
		if ( currentStep != null ) {
			currentStep = null;
		} else {
			throw new TaskException("Step already ended");
		}
	}
}
