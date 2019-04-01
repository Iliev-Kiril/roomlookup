package demo.ui;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = DefaultView.VIEW_NAME)
public class DefaultView extends VerticalLayout implements View {
	/*
	 * This view is registered automatically based on the @SpringView annotation. 
	 * As it has an empty string as its view name, it will be shown when navigating to the Homepage
	 */
    public static final String VIEW_NAME = "";

    @PostConstruct
    void init() {
        addComponent(new Label("The search function is not complete yet. It works only if you search for the exact <first name> or <last name> with capital first letter or the <room number>."));
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // the view is constructed in the init() method()
    }
}