package obberton13.IRC;

import obberton13.Util.Queue;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Obberton on 2/14/2016.
 */
public class IRCConnection {
	private ArrayList<IIRCMessageListener> listeners = new ArrayList<>();
	private Thread _readThread;
	private Thread _writeThread;
	private Socket _socket;
	private volatile boolean _running;
	private Queue<String> _writeQueue;
	private BufferedReader _reader;
	private final Object _readerLock = new Object();
	private BufferedWriter _writer;
	private final Object _writerLock = new Object();

	public IRCConnection(String server, int port) throws IOException {
		_socket = new Socket(server, port);
		_reader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
		_writer = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
		_running = true;
		_readThread = new Thread() {
			public void run() {
				while (_running) {
					synchronized(_readerLock) {
						try {
							String line = _reader.readLine();
							distributeMessage(line);
						} catch (IOException e) {
							e.printStackTrace();
							return;
						}
					}
				}
			}
		};
		_readThread.start();
		_writeQueue = new Queue<>();
		_writeThread = new Thread(){
			public void run()
			{
				while(_running)
				{
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
					synchronized(_writerLock)
					{
						if(!_writeQueue.isEmpty())
						{
							try {
								writeLine(_writeQueue.dequeue());
							} catch (IOException e) {
								e.printStackTrace();
								return;
							}
						}
					}
				}
			}
		};
	}

	public void registerMessageListener(IIRCMessageListener listener) {
		listeners.add(listener);
	}

	public void writeLine(String line) throws IOException {
		if (!line.startsWith("PASS")) {
			System.out.println(">>>" + line);
		} else {
			System.out.println(">>>PASS {hidden}");
		}
		synchronized (_writerLock) {
			_writer.write(line);
			_writer.newLine();
			_writer.flush();
		}
	}

	private void distributeMessage(String message) {
		System.out.println(message);
		for (IIRCMessageListener listener : listeners) {
			listener.messageReceived(message);
		}
	}

	public void close() {
		_running = false;
		_readThread.interrupt();
		_writeThread.interrupt();
		try {
			_reader.close();
			_writer.close();
			_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
