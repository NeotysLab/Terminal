package com.neotys.rte.TerminalEmulator;

import com.google.common.base.Optional;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by hrexed on 26/04/18.
 */
public final class GetBufferAction implements Action {
    private static final String BUNDLE_NAME = "com.neotys.rte.TerminalEmulator.bundle";
    private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayNameGetBuffer");
    private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");
    public static final String HOST="HOST";
    private static final ImageIcon LOGO_ICON;
    public static final String CleanOutput="CleanOutput";
    @Override
    public String getType() {
        return "GetBuffer";
    }

    @Override
    public List<ActionParameter> getDefaultActionParameters() {
        final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
        parameters.add(new ActionParameter(HOST,HOST));

        // TODO Add default parameters.
        return parameters;
    }
    static {
        final URL iconURL = OpenSessionAction.class.getResource("logo.png");
        if (iconURL != null) {
            LOGO_ICON = new ImageIcon(iconURL);
        }
        else {
            LOGO_ICON = null;
        }
    }
    @Override
    public Class<? extends ActionEngine> getEngineClass() {
        return GetBufferActionEngine.class;
    }

    @Override
    public Icon getIcon() {
        // TODO Add an icon
        return LOGO_ICON;
    }

    @Override
    public boolean getDefaultIsHit(){
        return true;
    }

    @Override
    public String getDescription() {
        final StringBuilder description = new StringBuilder();
        // TODO Add description
        description.append("GetBuffer Will display and clear the buffer . useful to display the buffer after a timeout.\n")
                .append("The parameters are : \n")
                .append("HOST  : host or ip of the server\n")
                .append("CleanOutput  : Optionnal ( default value true)  this parameter will clean the ouput screens\n");
        return description.toString();
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public String getDisplayPath() {
        return DISPLAY_PATH;
    }

    @Override
    public Optional<String> getMinimumNeoLoadVersion() {
        return Optional.of("6.2");
    }

    @Override
    public Optional<String> getMaximumNeoLoadVersion() {
        return Optional.absent();
    }
}
