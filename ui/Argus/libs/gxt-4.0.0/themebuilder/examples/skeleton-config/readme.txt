This config file lists all of the possible properties that the themer requires
and can make use of. It isn't a very helpful starting point since you have to
do everything from scratch, but it can be a handy reference for what fields are
there and what types they can take.

You can regenerate the basic structure of this file easily using the
-generateConfig flag to the themer and specifying a name:

Mac and Linux:
$ themer.sh -generateConfig skeleton-config.theme

Windows:
> themer.bat -generateConfig skeleton-config.theme



The generated config file (including the one in this directory) lists all required properties,
with default values, allowing you to customize them out individually, or create your own
relationships between them.
