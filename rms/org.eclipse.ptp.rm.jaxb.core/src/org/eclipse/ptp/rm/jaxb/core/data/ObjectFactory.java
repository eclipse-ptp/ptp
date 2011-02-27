//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.27 at 09:09:41 AM CST 
//

package org.eclipse.ptp.rm.jaxb.core.data;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the org.eclipse.ptp.rm.jaxb.core.data package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _ParserRef_QNAME = new QName("", "parser-ref"); //$NON-NLS-1$ //$NON-NLS-2$
	private final static QName _CommandRef_QNAME = new QName("", "command-ref");//$NON-NLS-1$ //$NON-NLS-2$
	private final static QName _Range_QNAME = new QName("", "range");//$NON-NLS-1$ //$NON-NLS-2$
	private final static QName _Monitor_QNAME = new QName("", "monitor");//$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: org.eclipse.ptp.rm.jaxb.core.data
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link Add }
	 * 
	 */
	public Add createAdd() {
		return new Add();
	}

	/**
	 * Create an instance of {@link AllAttributes }
	 * 
	 */
	public AllAttributes createAllAttributes() {
		return new AllAttributes();
	}

	/**
	 * Create an instance of {@link AllAttributes.Include }
	 * 
	 */
	public AllAttributes.Include createAllAttributesInclude() {
		return new AllAttributes.Include();
	}

	/**
	 * Create an instance of {@link Arg }
	 * 
	 */
	public Arg createArg() {
		return new Arg();
	}

	/**
	 * Create an instance of {@link Arglist }
	 * 
	 */
	public Arglist createArglist() {
		return new Arglist();
	}

	/**
	 * Create an instance of {@link AttributeDefinitions }
	 * 
	 */
	public AttributeDefinitions createAttributeDefinitions() {
		return new AttributeDefinitions();
	}

	/**
	 * Create an instance of {@link CancelJob }
	 * 
	 */
	public CancelJob createCancelJob() {
		return new CancelJob();
	}

	/**
	 * Create an instance of {@link ColumnData }
	 * 
	 */
	public ColumnData createColumnData() {
		return new ColumnData();
	}

	/**
	 * Create an instance of {@link Command }
	 * 
	 */
	public Command createCommand() {
		return new Command();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "command-ref")
	public JAXBElement<String> createCommandRef(String value) {
		return new JAXBElement<String>(_CommandRef_QNAME, String.class, null, value);
	}

	/**
	 * Create an instance of {@link Commands }
	 * 
	 */
	public Commands createCommands() {
		return new Commands();
	}

	/**
	 * Create an instance of {@link Control }
	 * 
	 */
	public Control createControl() {
		return new Control();
	}

	/**
	 * Create an instance of {@link Control.RunCommands }
	 * 
	 */
	public Control.RunCommands createControlRunCommands() {
		return new Control.RunCommands();
	}

	/**
	 * Create an instance of {@link DirectiveDefinition }
	 * 
	 */
	public DirectiveDefinition createDirectiveDefinition() {
		return new DirectiveDefinition();
	}

	/**
	 * Create an instance of {@link DirectiveDefinitions }
	 * 
	 */
	public DirectiveDefinitions createDirectiveDefinitions() {
		return new DirectiveDefinitions();
	}

	/**
	 * Create an instance of {@link DiscoverAttributes }
	 * 
	 */
	public DiscoverAttributes createDiscoverAttributes() {
		return new DiscoverAttributes();
	}

	/**
	 * Create an instance of {@link DiscoveredAttributes }
	 * 
	 */
	public DiscoveredAttributes createDiscoveredAttributes() {
		return new DiscoveredAttributes();
	}

	/**
	 * Create an instance of {@link DiscoveredAttributes.Include }
	 * 
	 */
	public DiscoveredAttributes.Include createDiscoveredAttributesInclude() {
		return new DiscoveredAttributes.Include();
	}

	/**
	 * Create an instance of {@link EnvironmentDefinition }
	 * 
	 */
	public EnvironmentDefinition createEnvironmentDefinition() {
		return new EnvironmentDefinition();
	}

	/**
	 * Create an instance of {@link EnvironmentDefinitions }
	 * 
	 */
	public EnvironmentDefinitions createEnvironmentDefinitions() {
		return new EnvironmentDefinitions();
	}

	/**
	 * Create an instance of {@link ExecuteCommand }
	 * 
	 */
	public ExecuteCommand createExecuteCommand() {
		return new ExecuteCommand();
	}

	/**
	 * Create an instance of {@link GridData }
	 * 
	 */
	public GridData createGridData() {
		return new GridData();
	}

	/**
	 * Create an instance of {@link GridLayout }
	 * 
	 */
	public GridLayout createGridLayout() {
		return new GridLayout();
	}

	/**
	 * Create an instance of {@link Group }
	 * 
	 */
	public Group createGroup() {
		return new Group();
	}

	/**
	 * Create an instance of {@link JobAttribute }
	 * 
	 */
	public JobAttribute createJobAttribute() {
		return new JobAttribute();
	}

	/**
	 * Create an instance of {@link LaunchTab }
	 * 
	 */
	public LaunchTab createLaunchTab() {
		return new LaunchTab();
	}

	/**
	 * Create an instance of {@link ManagedFile }
	 * 
	 */
	public ManagedFile createManagedFile() {
		return new ManagedFile();
	}

	/**
	 * Create an instance of {@link ManagedFiles }
	 * 
	 */
	public ManagedFiles createManagedFiles() {
		return new ManagedFiles();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "monitor")
	public JAXBElement<Object> createMonitor(Object value) {
		return new JAXBElement<Object>(_Monitor_QNAME, Object.class, null, value);
	}

	/**
	 * Create an instance of {@link OnShutDown }
	 * 
	 */
	public OnShutDown createOnShutDown() {
		return new OnShutDown();
	}

	/**
	 * Create an instance of {@link OnStartUp }
	 * 
	 */
	public OnStartUp createOnStartUp() {
		return new OnStartUp();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "parser-ref")
	public JAXBElement<String> createParserRef(String value) {
		return new JAXBElement<String>(_ParserRef_QNAME, String.class, null, value);
	}

	/**
	 * Create an instance of {@link Parsers }
	 * 
	 */
	public Parsers createParsers() {
		return new Parsers();
	}

	/**
	 * Create an instance of {@link PostExecuteCommands }
	 * 
	 */
	public PostExecuteCommands createPostExecuteCommands() {
		return new PostExecuteCommands();
	}

	/**
	 * Create an instance of {@link PreExecuteCommands }
	 * 
	 */
	public PreExecuteCommands createPreExecuteCommands() {
		return new PreExecuteCommands();
	}

	/**
	 * Create an instance of {@link Property }
	 * 
	 */
	public Property createProperty() {
		return new Property();
	}

	/**
	 * Create an instance of {@link Put }
	 * 
	 */
	public Put createPut() {
		return new Put();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "range")
	public JAXBElement<String> createRange(String value) {
		return new JAXBElement<String>(_Range_QNAME, String.class, null, value);
	}

	/**
	 * Create an instance of {@link ResourceManagerData }
	 * 
	 */
	public ResourceManagerData createResourceManagerData() {
		return new ResourceManagerData();
	}

	/**
	 * Create an instance of {@link ResumeJob }
	 * 
	 */
	public ResumeJob createResumeJob() {
		return new ResumeJob();
	}

	/**
	 * Create an instance of {@link RunBatch }
	 * 
	 */
	public RunBatch createRunBatch() {
		return new RunBatch();
	}

	/**
	 * Create an instance of {@link RunDebug }
	 * 
	 */
	public RunDebug createRunDebug() {
		return new RunDebug();
	}

	/**
	 * Create an instance of {@link RunInteractive }
	 * 
	 */
	public RunInteractive createRunInteractive() {
		return new RunInteractive();
	}

	/**
	 * Create an instance of {@link Script }
	 * 
	 */
	public Script createScript() {
		return new Script();
	}

	/**
	 * Create an instance of {@link Set }
	 * 
	 */
	public Set createSet() {
		return new Set();
	}

	/**
	 * Create an instance of {@link Site }
	 * 
	 */
	public Site createSite() {
		return new Site();
	}

	/**
	 * Create an instance of {@link StreamParser }
	 * 
	 */
	public StreamParser createStreamParser() {
		return new StreamParser();
	}

	/**
	 * Create an instance of {@link Style }
	 * 
	 */
	public Style createStyle() {
		return new Style();
	}

	/**
	 * Create an instance of {@link SuspendJob }
	 * 
	 */
	public SuspendJob createSuspendJob() {
		return new SuspendJob();
	}

	/**
	 * Create an instance of {@link TabController }
	 * 
	 */
	public TabController createTabController() {
		return new TabController();
	}

	/**
	 * Create an instance of {@link TabFolder }
	 * 
	 */
	public TabFolder createTabFolder() {
		return new TabFolder();
	}

	/**
	 * Create an instance of {@link TabItem }
	 * 
	 */
	public TabItem createTabItem() {
		return new TabItem();
	}

	/**
	 * Create an instance of {@link Token }
	 * 
	 */
	public Token createToken() {
		return new Token();
	}

	/**
	 * Create an instance of {@link Validator }
	 * 
	 */
	public Validator createValidator() {
		return new Validator();
	}

	/**
	 * Create an instance of {@link Viewer }
	 * 
	 */
	public Viewer createViewer() {
		return new Viewer();
	}

	/**
	 * Create an instance of {@link Viewer.InitialRows }
	 * 
	 */
	public Viewer.InitialRows createViewerInitialRows() {
		return new Viewer.InitialRows();
	}

	/**
	 * Create an instance of {@link Viewer.InitialRows.Row }
	 * 
	 */
	public Viewer.InitialRows.Row createViewerInitialRowsRow() {
		return new Viewer.InitialRows.Row();
	}

	/**
	 * Create an instance of {@link Widget }
	 * 
	 */
	public Widget createWidget() {
		return new Widget();
	}

	/**
	 * Create an instance of {@link Widget.Content }
	 * 
	 */
	public Widget.Content createWidgetContent() {
		return new Widget.Content();
	}

}
