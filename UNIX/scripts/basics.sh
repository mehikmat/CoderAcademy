UNIX--Uniplexed Information and Computing Service:

- Unix was originally developed in 1969 by a group of AT&T employees at Bell Labs, including Ken Thompson,
  Dennis Ritchie, Douglas McIlroy, and Joe Ossanna
- There are various Unix variants available in the market. Solaris Unix, AIX, HP Unix and BSD are few
  examples. Linux is also a flavor of Unix which is freely available,Linus Torvalds developed in 1991
- Several people can use a UNIX computer at the same time; hence UNIX is called a multiuser system.
- A user can also run multiple programs at the same time; hence UNIX is called multitasking.


Login Unix:
- When you first connect to a UNIX system, you usually see a prompt such as the following:
- login:

Change Password:
1. Type passwd at command prompt
2. Enter your old password
3. Type your new password
4. Verify the new password by typing it again

Listing Directories and Files:
- All data in UNIX is organized into files.
- All files are organized into directories.
- These directories are organized into a tree-like structure called the filesystem.
- You can use ls command to list out all the files or directories available in a directory
- Enteries starting with d..... represent directories, other represents files

Who Are You?:
- While you're logged into the system, you might be willing to know -- Who am I?
- just type command whoami

Who is Logged In?:
- Sometime you might be interested to know who is logged into the computer at the same time
- There are three commands are available to get you this information: users, who, and w
- Each command gives the diffrent level of information about currently logged in users

Logging Out:
- Type logout command at command prompt, and the system will clean up everything and break the
  connection

System Shutdown:
The most consistent way to shut down a Unix system properly via the command line is to use one of the following commands-
- halt: Brings the system down immediately.
- init 0: Powers off the system using predefined scripts to synchronize and clean up the system prior to shutdown
- init 6: Reboots the system by shutting it down completely and then bringing it completely back up
- poweroff:  Shuts down the system by powering off
- reboot:  Reboots the system
- shutdown: Shuts down the system

Unix File Management:



