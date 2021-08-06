This sample config and pom.xml file generate all of their sources in the
standard src/main/java directory when mvn -Pgenerate is invoked. This allows
developers to generate a theme based on some paramters, and then tweak it
manually, checking in the changes.



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