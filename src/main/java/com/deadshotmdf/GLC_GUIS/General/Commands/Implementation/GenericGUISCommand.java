package com.deadshotmdf.GLC_GUIS.General.Commands.Implementation;

import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.General.Commands.CommandType;
import com.deadshotmdf.GLC_GUIS.General.Commands.GLCGUICommand;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;

public class GenericGUISCommand extends GLCGUICommand<AbstractGUIManager> {

    public GenericGUISCommand(GLCGGUIS main, AbstractGUIManager manager, GuiManager guiManager) {
        super(main, manager);
        this.subCommands.put("glcopengui", new OpenGUI(manager, guiManager, "", CommandType.CONSOLE, 2, "/glcguis glcopengui [player] [gui]", "/glcguis glcopengui [player] [gui]"));
        this.subCommands.put("reload", new ReloadConfig(main, manager, "glcguis.reload", CommandType.BOTH, 0, "/glcguis reload", ""));
    }
}
