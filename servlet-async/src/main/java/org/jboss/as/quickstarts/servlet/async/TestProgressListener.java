package org.jboss.as.quickstarts.servlet.async;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.fileupload.ProgressListener;


@ServerEndpoint("/progress")
public class TestProgressListener implements ProgressListener {

	private long num100Ks = 0;

	private long theBytesRead = 0;
	private long theContentLength = -1;
	private int whichItem = 0;
	private int percentDone = 0;
	private boolean contentLengthKnown = false;

	private static Session session;

	public void update(long bytesRead, long contentLength, int items) {
		if (contentLength > -1) {
			contentLengthKnown = true;
		}
		theBytesRead = bytesRead;
		theContentLength = contentLength;
		whichItem = items;

		long nowNum100Ks = bytesRead / 100000;
		// Only run this code once every 100K
		if (nowNum100Ks > num100Ks) {
			num100Ks = nowNum100Ks;
			if (contentLengthKnown) {
				percentDone = (int) Math.round(100.00 * bytesRead / contentLength);
			}
			
			System.out.println(this.getWhichItem() + " " + getMessage());
			System.out.println(session);
			session.getAsyncRemote().sendText(this.getWhichItem() + " " + getMessage());
		}
	}
	
	@OnOpen
    public void onOpen(Session session) {
        // same as above
		TestProgressListener.session = session;
		System.out.println(session);
		System.out.println("Connect to progress");
		System.out.println(session.getId());
    }
	
    @OnMessage
    public void shout(String text, Session session) {
    	session.getAsyncRemote().sendText(text.toUpperCase());
        System.out.println(session.getId());
    }
    
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
       System.out.println(String.format("Session %s close because of %s", session.getId(), closeReason));
       
    }
	

	public String getMessage() {
		if (theContentLength == -1) {
			return "" + theBytesRead + " of Unknown-Total bytes have been read.";
		} else {
			return "" + theBytesRead + " of " + theContentLength + " bytes have been read (" + percentDone + "% done).";
		}

	}

	public long getNum100Ks() {
		return num100Ks;
	}

	public void setNum100Ks(long num100Ks) {
		this.num100Ks = num100Ks;
	}

	public long getTheBytesRead() {
		return theBytesRead;
	}

	public void setTheBytesRead(long theBytesRead) {
		this.theBytesRead = theBytesRead;
	}

	public long getTheContentLength() {
		return theContentLength;
	}

	public void setTheContentLength(long theContentLength) {
		this.theContentLength = theContentLength;
	}

	public int getWhichItem() {
		return whichItem;
	}

	public void setWhichItem(int whichItem) {
		this.whichItem = whichItem;
	}

	public int getPercentDone() {
		return percentDone;
	}

	public void setPercentDone(int percentDone) {
		this.percentDone = percentDone;
	}

	public boolean isContentLengthKnown() {
		return contentLengthKnown;
	}

	public void setContentLengthKnown(boolean contentLengthKnown) {
		this.contentLengthKnown = contentLengthKnown;
	}

}