package com.argus.client.programs.views.resultsdetailed;

import java.util.Objects;

import com.argus.client.programs.views.resultsdetailed.images.ResultsDetailedImages;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.info.Info;

public class ViewResultsDetailed extends Window {


	@SuppressWarnings("unchecked")
	public ViewResultsDetailed(final SimpleEventBus ArgusEventBus, Integer aKEY,String build) {

		final Window me = this;
		final VerticalLayoutContainer detailedVContainer = new VerticalLayoutContainer();
		final TestCasesContentPanel testCasesContentPanel = new TestCasesContentPanel(ArgusEventBus, aKEY);
		final SuitesContentPanel suitesContentPanel = new SuitesContentPanel(ArgusEventBus, aKEY, testCasesContentPanel);
		final ResultsDetailedToolBar resultsDetailedToolBar = new ResultsDetailedToolBar(ArgusEventBus, aKEY, testCasesContentPanel);
		

		//detailedVContainer.add(suitesContentPanel, new VerticalLayoutData(1, 300, new Margins(0, 0, 0, 0)));
		//detailedVContainer.add(resultsDetailedToolBar,       new VerticalLayoutData(1,    100, new Margins(0, 0, 0, 0)));
		//detailedVContainer.add(testCasesContentPanel,  new VerticalLayoutData(1,     400, new Margins(0, 0, 0, 0)));
		suitesContentPanel.getStore().addStoreAddHandler(new StoreAddHandler(){
			@Override
			public void onAdd(StoreAddEvent event) {
				Integer tSize = (suitesContentPanel.getStore().size()+1) * 22 + 6;
				if (tSize < 350) {
					detailedVContainer.add(suitesContentPanel, 			new VerticalLayoutData(1, tSize, new Margins(0, 0, 0, 0)));
					detailedVContainer.add(resultsDetailedToolBar,		new VerticalLayoutData(1,    -1, new Margins(0, 0, 0, 0)));
					detailedVContainer.add(testCasesContentPanel,  		new VerticalLayoutData(1,     1d, new Margins(0, 0, 0, 0)));
				} else {
					detailedVContainer.add(suitesContentPanel, 			new VerticalLayoutData(1,   0.4d, new Margins(0, 0, 0, 0)));
					detailedVContainer.add(resultsDetailedToolBar,      new VerticalLayoutData(1,    -1, new Margins(0, 0, 0, 0)));
					detailedVContainer.add(testCasesContentPanel,  		new VerticalLayoutData(1,     0.6d, new Margins(0, 0, 0, 0)));
				}
				
				me.forceLayout();
			}
		});

		this.setClosable(true);
		this.setMaximizable(true);
		this.setMinimizable(true);
		
		this.setHeading("RunResult:"+ build);
		this.getHeader().setIcon(ResultsDetailedImages.INSTANCE.bar_chart_16());
		this.add(detailedVContainer);

	}

}