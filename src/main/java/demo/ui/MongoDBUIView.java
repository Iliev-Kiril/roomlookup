package demo.ui;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import org.springframework.util.StringUtils;

import demo.data.Customer;
import demo.data.CustomerRepository;
import demo.ui.event.EventSystem;
import demo.ui.event.ReloadEntriesEvent;

@SpringView(name = MongoDBUIView.VIEW_NAME)
@UIScope
public class MongoDBUIView extends VerticalLayout implements View,ReloadEntriesEvent.ReloadEntriesListener{
	private static final long serialVersionUID = -785973470246010003L;
	public static final String VIEW_NAME = "Employees";
	private static final Log LOG = LogFactory.getLog(MongoDBUIView.class);
	
	private Grid<Customer> grid;
	private String selectedId;
	private Customer selectedCustomer;
	//private TextField filterBySearchText;
	private TextField filterByFirstName;
	private TextField filterByLastName;
	private TextField filterByRoomNumber;
	
	private Button deleteButton;
	private Button editButton;
	
	@Autowired
    private MongoDBContainer mongodbContainer;
    
    @Autowired
    private CustomerForm editForm;
    
    @Autowired
    private CustomerRepository service;
    
    @Autowired
    private EventSystem eventSystem;
	
	@PostConstruct
	void init() {
		registerEvents();
		initData();
		initLayout();
	}

	private void registerEvents() {
		eventSystem.addListener(this);
    }
	
	private void initLayout(){
		setMargin(true);
		setSpacing(true);
		
        // search bar
        final AbstractLayout searchBar = initSearchBar(service);
        searchBar.setWidth("100%");
        
        // vaadin table 
        grid = new Grid<Customer>(Customer.class);
        grid.setDataProvider(mongodbContainer);
        
        // set columns
        grid.setColumnOrder(mongodbContainer.PROPERTIES);
        
        Column<Customer, ?> column = grid.getColumn("id");
        column.setHidden(true);
        
        grid.setSelectionMode(SelectionMode.SINGLE);
        
        grid.setWidth("100%");
        grid.setHeight("350px");

        // table select listener
        grid.addSelectionListener(event -> {
        	
    	  if(event.getFirstSelectedItem().isPresent()){
				selectedCustomer = event.getFirstSelectedItem().get();
				selectedId = selectedCustomer.getId();
				  
				LOG.info("Selected item id {"+ selectedId+"}");
    		  }       
        });
        
        // button bar
        final AbstractLayout buttonBar = initButtonBar();
        buttonBar.setWidth("100%");
        
        // edit Form
        editForm.setVisible(false);
        
        addComponent(searchBar);
        addComponent(grid);
        addComponent(buttonBar);
        addComponent(editForm);
	}
	
	private AbstractLayout initButtonBar() {
        final HorizontalLayout buttonBar = new HorizontalLayout();

        buttonBar.setSpacing(true);

        final Button addButton = new Button("Add entry", event -> editForm.setVisible(true));
        editButton = new Button("Edit Entry", event -> editSelectedEntry());
        ConfirmDialogListener confirmDialogListener = new ConfirmDialogListener() {
			
			@Override
			public void yes() {
				removeSelectedEntry();
				
			}
			
			@Override
			public void no() {
				// do nothing
			}
		};
        deleteButton = new Button("Delete entry", event -> {
        	ConfirmDialog confirmDialog = new ConfirmDialog("Delete Entry?", true, confirmDialogListener);
        	UI.getCurrent().addWindow(confirmDialog);
        });

        buttonBar.addComponent(addButton);
        buttonBar.addComponent(editButton);
        buttonBar.addComponent(deleteButton);

        buttonBar.setComponentAlignment(addButton, Alignment.MIDDLE_LEFT);
        buttonBar.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
        buttonBar.setComponentAlignment(deleteButton, Alignment.MIDDLE_RIGHT);

        return buttonBar;
    }
	
	private AbstractLayout initSearchBar(CustomerRepository service) {
        final HorizontalLayout searchBar = new HorizontalLayout();

        searchBar.setSpacing(true);
        
        this.service = service;
        this.filterByFirstName = new TextField();
        this.filterByLastName = new TextField();
        this.filterByRoomNumber = new TextField();
        filterByFirstName.setPlaceholder("Filter by first name");
        filterByLastName.setPlaceholder("Filter by last name");
        filterByRoomNumber.setPlaceholder("Filter by room nr");
        //this.filterBySearchText = new TextField();
        //filterBySearchText.setPlaceholder("Filter by FN/LS/RN");
        
		// Replace listing with filtered content when user changes filter
        filterByFirstName.setValueChangeMode(ValueChangeMode.EAGER);
        filterByFirstName.addValueChangeListener(e -> listCustomersByFirstName(e.getValue()));
        
        filterByLastName.setValueChangeMode(ValueChangeMode.EAGER);
        filterByLastName.addValueChangeListener(e -> listCustomersByLastName(e.getValue()));
        
        filterByRoomNumber.setValueChangeMode(ValueChangeMode.EAGER);
        filterByRoomNumber.addValueChangeListener(e -> listCustomersByRoomNumber(e.getValue()));
        
        //filterBySearchText.setValueChangeMode(ValueChangeMode.EAGER);
        //filterBySearchText.addValueChangeListener(e -> listCustomersByFirstNameOrLastNameOrRoomNumber(e.getValue()));

        searchBar.addComponents(filterByFirstName, filterByLastName, filterByRoomNumber);
        //searchBar.addComponent(filterBySearchText);

        searchBar.setComponentAlignment(filterByFirstName, Alignment.MIDDLE_CENTER);
        searchBar.setComponentAlignment(filterByLastName, Alignment.MIDDLE_CENTER);
        searchBar.setComponentAlignment(filterByRoomNumber, Alignment.MIDDLE_CENTER);
        //searchBar.setComponentAlignment(filterBySearchText, Alignment.MIDDLE_CENTER);

        return searchBar;
    }
	
	void listCustomersByFirstName(String filterByFirstName) {
		if (StringUtils.isEmpty(filterByFirstName)) {
			grid.setItems(service.findAll());
		}
		else {
			grid.setItems(service.findByFirstNameLike(filterByFirstName));
		}
	}
	
	void listCustomersByLastName(String filterByLastName) {
		if (StringUtils.isEmpty(filterByLastName)) {
			grid.setItems(service.findAll());
		}
		else {
			grid.setItems(service.findByLastNameLike(filterByLastName));
		}
	}
	
	void listCustomersByRoomNumber(String filterByRoomNumber) {
		if (StringUtils.isEmpty(filterByRoomNumber)) {
			grid.setItems(service.findAll());
		}
		else {
			grid.setItems(service.findByRoomNumberLike(filterByRoomNumber));
		}
	}
	
//	void listCustomersByFirstNameOrLastNameOrRoomNumber(String filterByFirstNameOrLastNameOrRoomNumber) {
//		if (StringUtils.isEmpty(filterByFirstNameOrLastNameOrRoomNumber)) {
//			grid.setItems(service.findAll());
//		}
//		else {
//			grid.setItems(service.findByFirstNameOrLastNameOrRoomNumberLike(filterByFirstNameOrLastNameOrRoomNumber));
//		}
//	}
    
    private void editSelectedEntry() {
        if (selectedId != null) {
        	LOG.info("Edit Customer with id "+selectedId);
            editForm.setData(selectedCustomer);
            editForm.setVisible(true);
        }
    }
    
    private void removeSelectedEntry() {
        if (selectedId != null) {
        	LOG.info("Delete Customer with id "+selectedId);
            service.delete(selectedId);
            eventSystem.fire(new ReloadEntriesEvent());
        }
    }
	
	private void initData() {
		// load data
        List<Customer> all = service.findAll();
        LOG.info(all);
        // clear table
        Collection<Customer> items = mongodbContainer.getItems();
		items.clear();
        // set table data
        items.addAll(all);
    }
	
	@Override
	public void enter(ViewChangeEvent event) {
		// the view is constructed in the init() method()
	}

	@Override
	public void reloadEntries(ReloadEntriesEvent event) {
        LOG.info("Received reload event. Refreshing entry table!");
        initData();
        grid.getDataProvider().refreshAll();
	}

}
