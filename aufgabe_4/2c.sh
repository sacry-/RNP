#!/bin/bash
bash reset.sh

sudo /usr/sbin/iptables -A INPUT -p tcp -s 172.16.1.0/24 -m state --state NEW -j DROP
sudo /usr/sbin/iptables -A INPUT -s 172.16.1.0/24 -j ACCEPT

sudo /usr/sbin/iptables -A OUTPUT -d 172.16.1.0/24 -j ACCEPT

# switch DROP and ACCEPT

# do: telnet 172.16.1.18 51000
# and then: socat - udp-connect:172.16.1.18:51000
# to proof