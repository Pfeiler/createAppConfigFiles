import groovy.text.SimpleTemplateEngine

def engine = new SimpleTemplateEngine()

def dirConfig = 'target/classes/config'
def dirTemplates = 'target/classes/templates'
def dirTarget = 'target/conf'

// get the config directory
def configFiles = new File('.', dirConfig)

// iterate through each file of the config folder
configFiles.eachFile { configFile ->
	
	println "Process config file " + configFile.name
	
	Properties properties = new Properties()
	configFile.withInputStream {
		properties.load(it)
	}
	
	// get the templates directory
	def templateFiles = new File('.', dirTemplates)

	// iterate through each file of the templates folder
	templateFiles.eachFile{ file ->
		
		// bind variables
		def template = engine.createTemplate(file).make(properties)
		
		def environemnt = configFile.name.take(configFile.name.lastIndexOf('.'))
		def folder = new File('.', dirTarget + File.separator + environemnt)
		if(! folder.exists()) folder.mkdirs()
		
		println "Save new config file " + environemnt + File.separator + file.name
		
		// define and write destination
		def toFile = new File(folder, file.name)
		toFile.write(template.toString())
	}
	
}
