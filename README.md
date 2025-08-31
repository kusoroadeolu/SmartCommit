#  PROJECT DESCRIPTION 

### Smart Commit is a simple CLI tool which allows you to suggest commit messages if you're not feeling in the mood to write commit messages

## SETUP AND INSTRUCTIONS(WINDOWS ONLY)
#### Setting up smart commit is very simple. Before you do ensure you have docker or docker desktop properly set up on your device
#### 1. Clone the repo: Command -> git clone "https://github.com/kusoroadeolu/SmartCommit"
#### 2. In the directory you cloned the repo build the docker image -> Command: "docker build -t smartcommit ."
#### 3. Configure the .bat file to write commands globally and easily: 
#### a. Go to any directory in your device, 
#### b.Create a folder named bin and drop the provided .bat file in there, 
#### c. Open up system variables, go to Path and add the directory path(of the folder holding the .bat file not the dir path of the .bat file itself) to the Path system variables
#### d. To ensure you've set up everything properly, go to your cmd line and type in "where smartcommit". You should see the location of the bat file. If you do you've set it up properly

## COMMANDS
### Help Commands
#### smartcommit -h or smartcommit --help -> Shows the description of smart commit and all subcommands under it
#### smartcommit suggest -h or  suggest --help -> Shows the description of the smart commit suggest commands and all other command options under it
#### smartcommit direct-run -h or direct-run --help -> Shows the description of the smart commit direct-run commands and all other command options under it

### Init command
#### smartcommit --init -> Checks if the folder it is initialized in is a git repository and creates the config file
#### smartcommit --init -h or --init --help -> Shows the description of the smart commit init commands and all other command options under it
#### smartcommit --init -V or --init --version -> Shows the current version of the init command

### Suggest command 
#### smartcommit suggest -> Suggests a commit message based on git diffs staged for commit 
#### smartcommit suggest "summary" -> Suggests a commit message with less detailed git diff context (smartcommit suggest automatically defaults to this unless specified in your config file)
#### smartcommit suggest "detail" -> Suggests a commit message with more detailed git diff context
#### smartcommit suggest -h or suggest --help -> Shows the description of the smart commit suggest commands and all other command options under it
#### smartcommit suggest -V or suggest --version -> Shows the current version of the suggest command

### Direct run command
### NOTE: To use this command you have to get your Personal Access Token and paste it in the config file to allow git to authenticate th cli tool when pushing
#### smartcommit direct-run -> Runs 3 git commands (git add -> git commit with a generated message and git push to your remote repo) 
#### smartcommit direct-run -m "summary/detail" or direct-run --mode "summary/detail" -> Runs the direct-run command with a generated commit message
#### smartcommit direct-run -m "manual" -msg "CUSTOM COMMIT MESSAGE" or direct-run -m "manual" --message"CUSTOM COMMIT MESSAGE" -> Runs the direct-run command with a custom commit message written by you. Note that a message must be specified if you use the manual mode
#### smartcommit direct-run -h or direct-run --help -> Shows the description of the smart commit direct-run commands and all other command options under it
#### smartcommit direct-run -V or direct-run --version -> Shows the current version of the direct-run command

### MORE INFORMATION ON THE CONFIG FILE
#### You need a gemini api key to run this project. You can get one from https://aistudio.google.com/apikey and plug it into your config file
#### You need a PAT token to use the direct run command with https. You can get one from Settings -> Developer Settings -> PAT token on your GitHub account
#### You can choose which file extensions you want to exclude when generating diff commit messages by adding the file extensions in the exclude array in the config file. E.g. exclude: [".md"] -> This will exclude .md files from being processed when creating commit messages
#### You can also change your default commit context mode in your config file to prevent always setting it when you want to suggest commit messages

### FEATURES ILL CONSIDER FOR LATER
#### Global config files to avoid constant re-config unless the user explicitly enables it
#### SSH Based Auth instead of PAT 
#### More summary context modes
#### Allow users to tweak their commit message responses to better fit their use case
#### Allow users to push from their custom local git branch to their remote git branch

### TECHNOLOGIES USED
Java 21
Docker
Maven
JGit & Git

### CREATED BY
Kusoro Victor
Version 1.0.0

