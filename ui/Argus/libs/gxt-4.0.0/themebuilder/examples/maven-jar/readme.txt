This sample config and pom.xml file are meant to be a full maven project on
their own, and when mvn install or mvn deploy are run, will create and install/
deploy a jar file containing a finished theme, as defined by the config file.

As invoked in this pom, when the screenshot is taken, it is saved in the
target/ folder for later review, as is the manifest file. Similarly, a
war/ directory is created, with the compiled css3 sample app that was used to
generate the screenshot and manifest. This can be disabled by removing these
arguments from the java:exec plugin configuration.

The theme file is in this directory, though it probably should be in src/main/resources
or the like. The theme file used here is a copy of the quick-start file.


Below is a very brief summary/abridged of the config structure.
View online documentation for more information.

==== config ===
theme {
  /* First, create a name for your theme, and define a package to place it in */
  name = "sampletheme"
  basePackage = "com.example"

  ...

  /* Next, configure some basic defaults, to be used throughout the file */
  text = util.fontStyle("Tahoma, Arial, Verdana, sans-serif", "13px", "#000000", "normal")

  /* Here we expand on those initial values to prepare them for the main theme */
  borderColor = headerBgColor

  ...

  /* Finally, the theme itself, with a few presets, along with the values used above */
  details {

    ...

  }
}
==== config ===