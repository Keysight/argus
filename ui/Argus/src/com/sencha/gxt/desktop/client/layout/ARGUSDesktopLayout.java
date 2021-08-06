package com.sencha.gxt.desktop.client.layout;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.widget.core.client.Window;

public class ARGUSDesktopLayout extends LimitedDesktopLayout implements DesktopLayout {

	private static final int ICONS = 110;
	private static final int EXECUTION = 350;
	private static final int BORDER = 3;
	private static final int BUILD_MANAGER_HEIGHT = 500;
	private static final int BUILD_MANAGER_WIDTH = 700;

	@Override
	public DesktopLayoutType getDesktopLayoutType() {
		return DesktopLayoutType.ARGUS;
	}

	@Override
	public void layoutDesktop(Window requestWindow, RequestType requestType, Element element, Iterable<Window> windows,
			int containerWidth, int containerHeight) {

		super.layoutDesktop(requestWindow, requestType, element, windows, containerWidth, containerHeight);
	}

	@Override
	protected void layoutWindow(Window window, int containerWidth, int containerHeight, int width, int height) {

		String windowTitle = window.getHeading().asString();
		window.getHeader().getOffsetHeight();

		if (windowTitle.contains("Execution Engine")) {
			window.setPixelSize(EXECUTION, containerHeight);
			window.setPosition(ICONS, BORDER);
			window.setMinWidth(EXECUTION - 50);

		} else if (windowTitle.contains("Manufacturing Test")) {
			window.setPixelSize(containerWidth - (ICONS + BORDER + BORDER), containerHeight);
			window.setPosition(ICONS + BORDER, BORDER);
						
		} else if (windowTitle.equals("Linux Build Manager")) {
			window.setPixelSize(BUILD_MANAGER_WIDTH, BUILD_MANAGER_HEIGHT);
			window.setPosition(ICONS + EXECUTION + BORDER, BORDER);
			
		} else { 
			window.setPixelSize(containerWidth - (ICONS + EXECUTION + BORDER + BORDER), containerHeight);
			window.setPosition(ICONS + EXECUTION + BORDER, BORDER);
		}


	}

}
