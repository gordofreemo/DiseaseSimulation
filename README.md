# Project 4 - Disease Simulation
## General Overview
This project simulates the spread of disease over a population of so-called "Agents", represented by 
variously colored circles in the graphical display. The program uses command-line arguments to specify the 
configuration file and the three extra required features that are implemented are the history of the simulation,
a plot of the number of agents with various states at a given time, and being able to specify how many 
agents are initially immune at the start. 
<br> 
#### Agent States 
The agent states are represented by different colors in the GUI. The colors and their representations are:
  - Dark Blue - Vulnerable 
  - Red - Sick 
  - Light Blue - Immune 
  - Black - Dead 
---
These same states are used in the plot to represent how many agents have which state. For example, the red
line corresponds to how many agents are currently sick. 
#### Logging/History Display 
The logger can be enabled/disabled with the logenabled option in the config file (discussed later on). 
The logger gives you a text output alerting you as to when a given agent has changed its state. All the agents
are given id's when the program starts, but there's not a good way to see which agent has which id. The logger
function by having a timer task in the graphical display that takes messages out a queue that agents populate
with their logs. The log becomes nearly impossible to read and slows down the program if you have more 
than one thousand agents or so, so be careful. 
#### Plot of Agent States
The plot can be enabled/disabled with the logenabled option in the config file. The reason both the 
logger and the plot have to run at the same time is because the logging object is the only object 
alerted when an agent changes state, so it is the only thing capable of keeping track of that information.
The plot has four different color coded lines representing the various agents states. The y-axis represents
the total # of agents with a certain state and the x-axis is the number of cycles from the start of the 
simulation. 
<br>

As a small implementation detail, the agents communicate via a blocking queue of "messages", which is a functional 
interface that has a method that takes an agent and performs some action on it. These messages are then 
constructed dynamically in the agent class and that is the standard used for communication. 

## How to use the Program 
The main point of entry for this program is the "Display" class. It starts up the graphical interface 
and all the components necessary to run the program. <br>
To get the program to read the configuration file, you include the file name as a command line argument. <br> 
If a file is not provided, could not be found, or there is an error reading the file, the program will 
default to using the default options for running the simulation. If you do not specify a filename, you will 
not be alerted that a file has not found been, but if you did include command line arguments and the file 
was not able to be opened, you will be alerted with a message in the console. <br> 
There are three extra config file options that you can specify that weren't included in the project specification. <br> 
- logenabled t | f - this argument specifies whether the program should enable the graphing/logging functionality.
The two options are "t" and "f" for true and false respectively. By default, this is turned on, though 
the "manyAgents.txt" file in the resources folder has it turned off for performance reasons. 
My recommendation is to have it turned on when you have 1,000 or fewer agents, and off if more. 
- initialimmune n - This option was one of the extra 'required' features which allows you to specify how 
many agents are immune at the start of the program. By default, this is 0. 
- unittime ms - This specifies how long a "cycle" is in the program. This is easiest explained through an example.
If say you set the sickness to be 5, the agent will be sick for approximately unittime*5 ms. 
By default, this is set to be 150, I found that having this be slower makes the simulation look very choppy 
and a lot faster makes it hard to see what's going on. However, if you're using a lot of agents, unittime 200 
is a good option to set. This is what the "manyAgents.txt" file uses for its unit time. 
<br>
<br>
After starting up the program with a command line argument, all you need to do now is watch! 

## Group Aspect 
The "group" part of this project was kinda rough. I wasn't really able to get in touch with my partner, but the 
code he wrote is on another branch in this repository - the "Junyi-Code" branch. 

## Any Known Issues
The only real issue I've run into is the program will sometimes just stop. I have been able to gather 
that it can't be a threading issues, since if I print out the status of the program it runs fine, but it must
be some weird thing with JavaFX. Doesn't happen too often but if you run into, just run the program again,
and it should be fixed. Also, the GUI will break very fast at around the 10,000 agent mark, but I suspect
that something like that is expected. The "manyAgents.txt" text file has 7,000 agents and runs fine with 
the graph/logger off. 
## Project Log 

### Andrew 
DONE:

- Made AgentManager class to handle initialization of Agents
- Agents appear to function, message sending/disease spreading works fine
- Made AgentDrawer class to draw agents on some screen
- Started a bit of the display

TO DO: 
- Make PDF design diagram 


