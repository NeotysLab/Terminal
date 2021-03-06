package com.neotys.rte.TerminalEmulator.telnet;

import com.neotys.extensions.action.engine.Context;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetInputListener;

import java.io.IOException;
import java.io.OutputStream;

public class TelnetSession {
    private final TelnetClient session;
    private Context context;
    private TelnetSession(final TelnetClient session,Context context) {

        this.session = session;
        this.context=context;
    }

    public static TelnetSession of(final String host, final int port,final String terminalType,int timeout ,Context context) throws TelnetClientException {
        return new TelnetSession(TelnetConnector.INSTANCE.createSession(host, port,terminalType,timeout),context);
    }

    public TelnetClient getclient()
    {
        return session;
    }
    public boolean isConnected() {
        return session.isConnected();
    }

    /**
     * Close ALL channels and Session at the same time
     */
    public void close() throws IOException {
        session.disconnect();

    }

    public TelnetChannel createChannel() throws TelnetSessionException {
        return TelnetChannel.of(session,context);
    }
}

