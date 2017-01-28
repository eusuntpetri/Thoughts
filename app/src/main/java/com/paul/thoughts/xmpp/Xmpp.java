package com.paul.thoughts.xmpp;

import android.util.Log;

import com.paul.thoughts.exception.ConnectionException;
import com.paul.thoughts.util.Constants;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds all necessary XMPP-related configuration objects.
 */
public class Xmpp {

    public static final Instance INSTANCE = new Instance();

    public static Instance getInstance() {
        return INSTANCE;
    }

    /**
     * The object responsible for all XMPP operations.
     */
    public static class Instance {

        private AbstractXMPPConnection connection;
        private ChatManager chatManager;
        private ChatMessageListener chatMessageListener;
        private Map<String, Chat> chatsByUsername;

        public void init(String name, String password) {
            connection = Connection.get(name, password);
            connection.addConnectionListener(new ConnectionListener());
            chatsByUsername = new HashMap<>();
        }

        public void startListening(ChatMessageListener listener) {
            chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addChatListener(new ChatManagerListener(listener));
            this.chatMessageListener = listener;
        }

        /**
         * Attempts to connect using the presumably pre-configured {@link AbstractXMPPConnection}
         * object.
         *
         * @throws ConnectionException When the connection attempt fails.
         */
        public void connect() throws ConnectionException {
            try {
                connection.connect();
            } catch (XMPPException | SmackException | IOException e) {
                throw new ConnectionException("Connection failed!", e);
            }
        }

        /**
         * Attempts to log in to the XMPP server using the presumably pre-configured and connected
         * {@link AbstractXMPPConnection} object.
         */
        public void login() {
            try {
                connection.login();
            } catch (XMPPException | IOException | SmackException e) {
                throw new ConnectionException("Login failed!", e);
            }

        }

        /**
         * Attempts to send a {@link Character} to the user identified by the provided
         * {@param username}. If a {@link Chat} already exists between the two users, it will
         * be re-used, otherwise a new one will be created.
         *
         * @param username  Username of the user for whom the message is intended.
         * @param character The {@link Character} to be sent to that user.
         */
        public void sendCharacterToUser(String username, Character character) {
            Chat chat = chatsByUsername.get(username);
            if (chat == null) {
                chat = chatManager.createChat(username, chatMessageListener);
                chatsByUsername.put(username, chat);
            }
            try {
                chat.sendMessage(character.toString());
            } catch (SmackException.NotConnectedException e) {
                throw new ConnectionException("XMPP connection must be established first!");
            }
        }
    }

    /**
     * Class that acts as a {@link Connection} provider, as well as a holder for the
     * {@link Connection} configuration.
     */
    public static class Connection {

        /**
         * Returns a pre-configured {@link AbstractXMPPConnection} object.
         *
         * @return The pre-configured {@link AbstractXMPPConnection}.
         */
        public static AbstractXMPPConnection get(String name, String password) {
            return new XMPPTCPConnection(getConfiguration(name, password));
        }

        /**
         * Returns an object containing configuration details for the XMPP {@link Connection}s.
         *
         * @return The {@link XMPPTCPConnectionConfiguration}.
         */
        public static XMPPTCPConnectionConfiguration getConfiguration(String name, String password) {
            XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
            configBuilder.setUsernameAndPassword(name, password);
            configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
            configBuilder.setResource(Constants.RESOURCE);
            configBuilder.setServiceName(Constants.DOMAIN);
            configBuilder.setHost(Constants.HOST);
            configBuilder.setPort(Constants.PORT);
            //configBuilder.setDebuggerEnabled(true);
            return configBuilder.build();
        }
    }

    /**
     * Class that acts as a listener for {@link Connection}-related events.
     */
    public static class ConnectionListener implements org.jivesoftware.smack.ConnectionListener {

        public void connected(XMPPConnection connection) {
            Log.d("DEBUG", "connected!");
        }

        public void connectionClosed() {
            Log.d("DEBUG", "connection closed!");
        }

        public void connectionClosedOnError(Exception arg0) {
            Log.d("DEBUG", "Connection closed on error!");
        }

        public void reconnectingIn(int arg0) {
            Log.d("DEBUG", "Reconnecting in " + arg0);
        }

        public void reconnectionFailed(Exception arg0) {
            Log.d("DEBUG", "Reconnection failed!");
        }

        public void reconnectionSuccessful() {
            Log.d("DEBUG", "Reconnection successful!");
        }

        public void authenticated(XMPPConnection arg0, boolean arg1) {
            Log.d("DEBUG", "Authenticated!");
        }
    }

    /**
     * Class that listens for {@link ChatManager}-related events.
     */
    public static class ChatManagerListener implements org.jivesoftware.smack.chat.ChatManagerListener {

        private final ChatMessageListener chatMessageListener;

        ChatManagerListener(ChatMessageListener listener) {
            this.chatMessageListener = listener;
        }

        /**
         * When a foreign {@link Chat} of local interest is created, a specific
         * {@link org.jivesoftware.smack.chat.ChatMessageListener} is attached to it, so as to
         * correctly process the messages received through it.
         *
         * @param chat           The newly-created {@link Chat}.
         * @param createdLocally True if the {@param chat} was created locally, eliminating the
         *                       need to attach a listener.
         */
        @Override
        public void chatCreated(Chat chat, boolean createdLocally) {
            if (!createdLocally) {
                chat.addMessageListener(chatMessageListener);
                INSTANCE.chatsByUsername.put(chat.getParticipant(), chat);
            }
        }
    }

    public class Administration {



    }

}
