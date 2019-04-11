# CSV-to-JSON+Chart
    Create a class that computes the
                             Top 10 traffic receivers, 
			         Top 10 traffic transmitters, 
				 Top 3 used protocols, 
 				 Top 10 used applications 
    using NetFlow data taken from the supplied archive(attached CSV file).

**Solution:**

1. Read CSV file

2. computeTop() saves JSON to **top.txt** 

**See** json_topReceivers.png.png      json_topTransmiters.png topApps.png

Using time range as an Input parameter calculate data for and numbers for traffic, protocols and application counts.

3. timeRange() saves JSON to **resume.txt**  

**See** traffic.png    traffic2.png 


Using computation results make a GUI window with a chart 
for received and transmitted bytes over time for top network transmitter and receiver.

4. Chart created using **org.jfree**

**See results in pictures**

receivers.png      transmitters.png

(*due to huge difference in traffic some hosts are not comparable on one chart*)

TransmitersScale.png      receiversScale.png

*For example single hosts can be also showed on chart*

ns_inside_local.png    web_server_local.png








   
