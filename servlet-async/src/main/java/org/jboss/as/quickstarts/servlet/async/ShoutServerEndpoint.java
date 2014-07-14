/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.quickstarts.servlet.async;

import java.util.concurrent.CountDownLatch;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/shout")
public class ShoutServerEndpoint {

	private static CountDownLatch latch;
	
	@OnOpen
    public void onOpen(Session session) {
        // same as above
		System.out.println("Connect");
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
        latch.countDown();
    }
}
