#!/bin/bash
bash reset.sh

sudo /usr/sbin/iptables -A OUTPUT -p icmp --icmp-type echo-request -j ACCEPT
sudo /usr/sbin/iptables -A INPUT -p icmp --icmp-type echo-request -j DROP

# ping to parallel pc should work
# ping from parallel pc should not work