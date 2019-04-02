package demo.ui;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import demo.ui.event.EventSystem;
import demo.ui.event.ReloadEntriesEvent;

@Theme("valo")
@Title("Roomlookup")
@SpringUI
@SpringViewDisplay
public class MyVaadinUI extends UI implements ViewDisplay{

	private Panel springViewDisplay;
	
	Image loginLogo;
	//String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	
	@Autowired
	EventSystem eventSystem;

	@Override
	protected void init(VaadinRequest request) {
		initLayout();
		registerEvents();
	}

	private void registerEvents() {
		eventSystem.registerEvent(ReloadEntriesEvent.ReloadEntriesListener.class, ReloadEntriesEvent.class);
	}

	private void initLayout() {
		final VerticalLayout root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.setSpacing(true);
		setContent(root);
		
		loginLogo.setSource(new ClassResource(String.format("/images/roomlookup_b.png", 1)));
		//loginLogo = new Image("", new FileResource(new File(basepath +  "/WEB-INF/images/roomlookup_b.png")));
		loginLogo.setWidth(30, Unit.PERCENTAGE);
		root.addComponent(loginLogo);
		root.setComponentAlignment(loginLogo, Alignment.MIDDLE_CENTER);

		final CssLayout navigationBar = new CssLayout();
		navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		
        navigationBar.addComponent(createNavigationButton("Employees",
                MongoDBUIView.VIEW_NAME));
        
		root.addComponent(navigationBar);

		springViewDisplay = new Panel();
        springViewDisplay.setSizeFull();
        root.addComponent(springViewDisplay);
        root.setExpandRatio(springViewDisplay, 1.0f);

	}

	private Button createNavigationButton(String caption, final String viewName) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
        return button;
    }
	
	@Override
    public void showView(View view) {
        springViewDisplay.setContent((Component) view);
    }
}