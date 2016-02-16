package obberton13;

import obberton13.IRC.IRCClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) {
		IRCClient client = new IRCClient("irc.twitch.tv", 6667);
		client.sendCredentials(null, args[1], args[0]);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		String currentchannel = null;
		try {
			while(!(line=reader.readLine()).startsWith("exit"))
			{
				if(line.startsWith("connect"))
				{
					if(currentchannel != null)
					{
						client.part(currentchannel);
					}
					currentchannel = line.substring(8);
					client.join(currentchannel);
				}
				else{
					if(currentchannel != null)
					{
						client.privmsg(currentchannel, line);
					}
				}
			}
			reader.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
