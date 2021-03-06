package com.neotys.rte.TerminalEmulator;

import java.util.List;

import com.google.common.base.Strings;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import com.neotys.rte.TerminalEmulator.ssh.SSHChannel;

/**
 * Created by hrexed on 26/04/18.
 */
public class SendSpecialKeyActionEngine  implements ActionEngine {
    String Host=null;

    String Key=null;
    String STimeOut;
    int TimeOut;
    boolean ClearBufferBefore=false;

    public SampleResult execute(Context context, List<ActionParameter> parameters) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();
        String sClearBufferBefore=null;

        //sess=null;
        for(ActionParameter parameter:parameters) {
            switch(parameter.getName())
            {
                case  SendSpecialKeyAction.HOST:
                    Host= parameter.getValue();
                    break;
                case  SendSpecialKeyAction.KEY:
                    Key = parameter.getValue();
                    break;

                case  SendSpecialKeyAction.TimeOut:
                    STimeOut = parameter.getValue();
                    break;
                case  SendSpecialKeyAction.ClearBufferBefore:
                    sClearBufferBefore = parameter.getValue();
                    break;
            }
        }

        if (Strings.isNullOrEmpty(Host)) {
            return getErrorResult(context, sampleResult, "Invalid argument: Host cannot be null "
                    + SendSpecialKeyAction.HOST + ".", null);
        }

        if (Strings.isNullOrEmpty(STimeOut)) {
            return getErrorResult(context, sampleResult, "Invalid argument: TimeOut cannot be null "
                    + SendSpecialKeyAction.TimeOut + ".", null);
        }
        else
        {
            try{
                TimeOut=Integer.parseInt(STimeOut);
            }
            catch (NumberFormatException e)
            {
                return getErrorResult(context, sampleResult, "Invalid argument: TimeOut needs to be a digit "
                        + SendSpecialKeyAction.TimeOut + ".", null);
            }
        }


        if (Strings.isNullOrEmpty(Key)) {
            return getErrorResult(context, sampleResult, "Invalid argument: Key cannot be null "
                    + SendSpecialKeyAction.KEY + ".", null);
        }
        else
        {
            if(!SSHChannel.isKeyInSpecialKeys(Key))
                return getErrorResult(context, sampleResult, "Invalid argument: Key Can only have the following values : CR,VT,ESC,DEL,BS,LF,HT "
                        + SendSpecialKeyAction.KEY + ".", null);
        }
        if (Strings.isNullOrEmpty(sClearBufferBefore)) {
            ClearBufferBefore=false;
        }
        else {
            if (sClearBufferBefore.equalsIgnoreCase("TRUE"))
                ClearBufferBefore = true;
            else
                ClearBufferBefore = false;
        }

        try {


            final SSHChannel channel = (SSHChannel)context.getCurrentVirtualUser().get(Host+"SSHChannel");
            if(channel != null)
            {
                if (channel.isConnected())
                {
                    try
                    {
                        sampleResult.sampleStart();
                        final String output = channel.sendSpecialKeys(Key, TimeOut,ClearBufferBefore);
                        sampleResult.sampleEnd();
                        appendLineToStringBuilder(responseBuilder, output);
                    }
                    catch (RTETimeOutException e) {
                        return getErrorResult(context, sampleResult, "TimeOut Exception:  "
                                , null);
                    }
                    catch (Exception e) {
                        return getErrorResult(context, sampleResult, "Technical Error:  "
                                , e);
                    }
                }
                else
                    return getErrorResult(context, sampleResult, "Session Error: The session is currently closed "
                            , null);
            }
            else
                return getErrorResult(context, sampleResult, "Session Error: No session created on that host "
                        , null);

        }
        catch (Exception e)
        {
            return getErrorResult(context, sampleResult, "Technical Error: "+e.getMessage(), e);
        }
        sampleResult.setRequestContent(requestBuilder.toString());
        sampleResult.setResponseContent(responseBuilder.toString());
        return sampleResult;
    }
    private void appendLineToStringBuilder(final StringBuilder sb, final String line){
        sb.append(line).append("\n");
    }

    /**
     * This method allows to easily create an error result and log exception.
     */
    private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
        result.setError(true);
        result.setStatusCode("NL-SendSpecialKey_ERROR");
        result.setResponseContent(errorMessage);
        if(exception != null){
            context.getLogger().error(errorMessage, exception);
        } else{
            context.getLogger().error(errorMessage);
        }
        return result;
    }

    @Override
    public void stopExecute() {
        // TODO add code executed when the test have to stop.
    }
}

