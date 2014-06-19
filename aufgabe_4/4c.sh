#!/bin/bash

bash reset.sh

# All DROP
sudo /usr/sbin/iptables -P INPUT DROP
sudo /usr/sbin/iptables -P OUTPUT DROP

sudo /usr/sbin/iptables -A INPUT -p tcp --sport 80 -s www.dmi.dk -j ACCEPT
sudo /usr/sbin/iptables -A INPUT -p tcp --sport 443 -s www.dmi.dk -j ACCEPT

sudo /usr/sbin/iptables -A OUTPUT -p tcp --dport 80 -d www.dmi.dk -j ACCEPT
sudo /usr/sbin/iptables -A OUTPUT -p tcp --dport 443 -d www.dmi.dk -j ACCEPT



# OUTPUT
# ACCEPT     tcp  --  anywhere             5.56.149.239         tcp dpt:http
# ACCEPT     tcp  --  anywhere             130.226.71.226       tcp dpt:http
# ACCEPT     tcp  --  anywhere             130.226.71.229       tcp dpt:http
# ACCEPT     tcp  --  anywhere             5.56.149.238         tcp dpt:http
# ACCEPT     tcp  --  anywhere             5.56.149.238         tcp dpt:https
# ACCEPT     tcp  --  anywhere             5.56.149.239         tcp dpt:https
# ACCEPT     tcp  --  anywhere             130.226.71.226       tcp dpt:https
# ACCEPT     tcp  --  anywhere             130.226.71.229       tcp dpt:https

# changes to ip address:
# restart the command





