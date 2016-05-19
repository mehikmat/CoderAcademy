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
1. Type passwd at command prompt.
2. Enter your old password.
3. Type your new password.
4. Verify the new password by typing it again.

Listing Directories and Files:
- You can use `ls`,`ls -a` and `ls -l`(for more) command to list out all the files or directories available in a directory.
- Enteries starting with d..... represent directories, other represents files.

Who Are You?:
- While you're logged into the system, you might be willing to know -- Who am I?
- just type command `whoami`

Who is Logged In?:
- Sometime you might be interested to know who is logged into the computer at the same time.
- There are three commands are available to get you this information: `users`, `who`, and `w`.
- Each command gives the diffrent level of information about currently logged in users.

Logging Out:
- Type logout command at command prompt, and the system will clean up everything and break the
  connection.

System Shutdown:
The most consistent way to shut down a Unix system properly via the command line is to use one of the following commands-
- halt: Brings the system down immediately.
- init 0: Powers off the system using predefined scripts to synchronize and clean up the system prior to shutdown.
- init 6: Reboots the system by shutting it down completely and then bringing it completely back up.
- poweroff:  Shuts down the system by powering off.
- reboot:  Reboots the system.
- shutdown: Shuts down the system.

Unix File Management:
- All data in UNIX are organized into files.
- All files are organized into directories.
- These directories are organized into a tree-like structure called the filesystem.
- In UNIX there are three basic types of files:
  1. Ordinary Files: A file on the system that contains data, text, or program instructions.
  2. Directories: Directories store both special and ordinary files.
  3. Special Files: Files which provide access to hardware such as hard drives,CD-ROM drives,modems,and Ethernet adapters.
- Single dot .  --- This represents current directory.
- Double dot .. --- This represents parent directory.


Creating Files:
- vi or vim or gedit to create/edit ordinary files.
- touch command to create a blak file.
- head/tail/more/less command to read parts of files.
- cat and cat -b (with line number) to read files.
- wc to count words and wc -l to count lines in file.
- cp for copy, rm for deleting, mv for moving files with -r/rm -rf option for directories.
- mkdir for creating directory and mkdir -p for creating dir with parent dirs and rmdir for deleting empty dir.

Standard Unix Streams:
- Under normal circumstances every Unix program has three streams (files) opened for it when it starts up:
  1. stdin : This is referred to as standard input and associated file descriptor is 0.
  2. stdout : This is referred to as standard output and associated file descriptor is 1.
  3. stderr : This is referred to as standard error and associated file descriptor is 2.
- eg. sh program_name 0< in_file 2> out_file 3> err_file.

The Permission Indicators:
- File ownership is an important component of UNIX that provides a secure method for storing files.
- Every file in UNIX has the following attributes:
  1. Owner permissions:4+2+1,0+0+0,0+0+0   --- 700
  2. Group permissions:4+2+1,4+2+1,+0+0+0  --- 770
  3. Other (world) permissions: 4+2+1,4+2+1,4+2+1 --- 777
- The permissions are broken into groups of threes, and each position in the group denotes a specific permission,
  in this order: read (r), write (w), execute (x):4-6-1
- chmod is used to change the permission: + for assigning designated permission, - for removing and = for setting permission.
  eg. chmod +wx
- chown: The chown command stands for "change owner" and is used to change the owner of a file.
- chgrp: The chgrp command stands for "change group" and is used to change the group of a file.

Unix Environment:
- An important Unix concept is the environment, which is defined by environment variables.
- Some are set by the system, others by you, yet others by the shell, or any program that loads another program.
- A variable is a character string to which we assign a value. The value assigned could be a number, text, filename,
  device, or any other type of data.
- Environment variables are set without using $ sign but while accessing them we use $sign as prefix.
- These variables retain their values until we come out ofshell.
- When you login to the system, the shell undergoes a phase called initialization to set up various environments,
  The process is as follows:
	1. The shell checks to see whether the file /etc/profile exists.
	2. If it exists, the shell reads it. Otherwise, this file is skipped. No error message is displayed.
	3. The shell checks to see whether the file .profile/.bashrc exists in your home directory. Your home directory is the
	   directory that you start out in after you log in.
	4. If it exists, the shell reads it; otherwise, the shell skips it. No error message is displayed.
  As soon as both of these files have been read, the shell displays a prompt: $
- The characters that the shell displays as your command prompt are stored in the variable PS1, you can change.

Java Basic Utilities:
- pr command:The pr command does minor formatting of files on the terminal screen or for a printer.
             eg. pr -2 -h "Restaurants" food.txt --- this makes a two-column file with the header Restaurants.
- The command lp or lpr prints a file onto paper as opposed to the screen display.
- he lpstat and lpq command shows what's in the printer queue.
- cancel and lprm commands terminate the printing request.
- You use the Unix mail command to send and receive mail. Here is the syntax to send an email:
  $mail -s message_texts [-s subject] [-c cc-addr] [-b bcc-addr] to-addr

Unix Pipes and Filters:
- 




