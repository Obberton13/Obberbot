package obberton13.IRC;

import java.io.IOException;

/**
 * Created by Obberton on 2/14/2016.
 */
public class IRCClient implements IIRCMessageListener {
	private IRCConnection _connection;

	public IRCClient(String server, int port) {
		try {
			_connection = new IRCConnection(server, port);
			_connection.registerMessageListener(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendCredentials(String user, String pass, String nick)
	{
		try {
			if (pass != null) {
				_connection.writeLine("PASS " + pass);
			}
			if (user != null) {
				_connection.writeLine("USER " + user);
			}
			if (nick != null) {
				_connection.writeLine("NICK " + nick);
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public void join(String channel) {
		if(!channel.startsWith("#"))
		{
			channel = "#" + channel;
		}
		try {
			_connection.writeLine("JOIN " + channel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void part(String channel) {
		if(!channel.startsWith("#"))
		{
			channel = "#" + channel;
		}
		try {
			_connection.writeLine("PART " + channel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void privmsg(String channel, String message)
	{
		if(!channel.startsWith("#"))
		{
			channel = "#" + channel;
		}
		try {
			_connection.writeLine("PRIVMSG " + channel + " :" + message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void messageReceived(String message) {
		System.out.println("CLIENT: " + message);
		if(message.startsWith("PING"))
		{
			try {
				_connection.writeLine("PONG " + message.substring(5));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//TODO parse the message and do commands based on that.
	}

	public void close()
	{
		_connection.close();
	}
}
