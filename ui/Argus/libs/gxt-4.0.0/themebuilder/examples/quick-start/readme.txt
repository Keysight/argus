This config file starts with less than ten properties, and uses those to
generate a full theme. This can be an easy way to get started, customizing
a few basic properties to get a theme, and then digging deeper into the rest
of the config file as necessary to make more specific changes.



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