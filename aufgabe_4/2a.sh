#!/bin/bash
bash reset.sh

# delete old rules
# sudo /usr/sbin/iptables -D INPUT -s 172.16.1.0/24 -j ACCEPT
# sudo /usr/sbin/iptables -D OUTPUT -d 172.16.1.0/24 -j ACCEPT

# block access
sudo /usr/sbin/iptables -I INPUT -s 172.16.1.0/24 -j DROP
sudo /usr/sbin/iptables -I OUTPUT -d 172.16.1.0/24 -j DROP


