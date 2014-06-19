#Source
sudo /sbin/route add -net 192.168.18.0/24 gw 192.168.17.1 dev eth1

#Target
sudo /sbin/route add -net 192.168.17.0/24 gw 192.168.18.1 dev eth1

# let x be the last digits of current source/target pc
# source: ping -s 1000 -c 4 192.168.x.x -W 1
# target: ping -s 1000 -c 4 192.168.x.x -W 1