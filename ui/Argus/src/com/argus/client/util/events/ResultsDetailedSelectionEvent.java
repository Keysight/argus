package com.argus.client.util.events;


import com.google.gwt.event.shared.GwtEvent;

public class ResultsDetailedSelectionEvent extends GwtEvent<ResultsDetailedSelectionHandler>{

	public static Type<ResultsDetailedSelectionHandler> TYPE = new Type<ResultsDetailedSelectionHandler>();
	private Integer selectedCount;
	
	public ResultsDetailedSelectionEvent(Integer selectedCount) {
		this.setSelectedCount(selectedCount);
	}

	@Override
	public Type<ResultsDetailedSelectionHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ResultsDetailedSelectionHandler handler) {
		handler.onTestCaseSelectionChnage(this);
	}

	public Integer getSelectedCount() {
		return selectedCount;
	}

	private void setSelectedCount(Integer selectedCount) {
		this.selectedCount = selectedCount;
	}


}
