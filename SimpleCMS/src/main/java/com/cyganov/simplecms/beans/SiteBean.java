package com.cyganov.simplecms.beans;

import com.cyganov.simplecms.converters.SectionConverter;
import com.cyganov.simplecms.domain.Content;
import com.cyganov.simplecms.domain.dto.SectionDto;
import com.cyganov.simplecms.services.SectionService;
import org.richfaces.component.UITree;
import org.richfaces.event.TreeSelectionChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tsyhanou Siarhei
 * Date: 21.08.13
 * Time: 10:17
 */

@Component
@Scope(value = "session")
public class SiteBean {

	@Autowired
	private SectionService sectionService;

	private SectionDto currentSelection = null;
	private List<SectionDto> sectionList;
	private boolean view;
	private String status;

	@PostConstruct
	public void load(){
		sectionList = SectionConverter.getSectionDtoList(sectionService.getSections());
		if (currentSelection == null && sectionList.size() != 0){
			currentSelection = sectionList.get(0);
		}
		view = true;
	}

	public void selectionChanged(TreeSelectionChangeEvent selectionChangeEvent) {
		List<Object> selection = new ArrayList<Object>(selectionChangeEvent.getNewSelection());
		Object currentSelectionKey = selection.get(0);
		UITree tree = (UITree) selectionChangeEvent.getSource();
		Object storedKey = tree.getRowKey();
		tree.setRowKey(currentSelectionKey);
		currentSelection = (SectionDto) tree.getRowData();
		tree.setRowKey(storedKey);
		view = true;
	}

	public void submit(){
		String parentId = null;
		if (currentSelection.getParent() != null){
			parentId = currentSelection.getParent().getId();
		}
		sectionService.updateSection(SectionConverter.DtoToSection(currentSelection), parentId);
		load();
	}

	public void newSection(){
		SectionDto section = new SectionDto();
		Content content = new Content();
		section.setContent(content);
		section.setParent(currentSelection);
		currentSelection = section;
		addStatus();
	}

	public void newRootSection(){
		SectionDto section = new SectionDto();
		Content content = new Content();
		section.setContent(content);
		currentSelection = section;
		addStatus();
	}

	public void delete(){
		sectionService.deleteSectionById(currentSelection.getId());
		currentSelection = null;
		load();
	}
	public void addStatus(){
		status = "Add New";
		view = false;
	}

	public void editStatus(){
		status = "Edit";
		view = false;
	}

	public SectionDto getCurrentSelection() {
		return currentSelection;
	}

	public void setCurrentSelection(SectionDto currentSelection) {
		this.currentSelection = currentSelection;
	}

	public List<SectionDto> getSectionList() {
		return sectionList;
	}

	public void setSectionList(List<SectionDto> sectionList) {
		this.sectionList = sectionList;
	}

	public boolean isView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
