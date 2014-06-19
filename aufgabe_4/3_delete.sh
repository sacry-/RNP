# Router
# Source
sudo /sbin/route del -net 192.168.18.0/24 gw 192.168.17.2 dev eth1

# Target
sudo /sbin/route del -net 192.168.17.0/24 gw 192.168.18.2 dev eth1

# ISDN
# Source
sudo /sbin/route del -net 192.168.18.0/24 gw 192.168.17.1 dev eth1

# Target
sudo /sbin/route del -net 192.168.17.0/24 gw 192.168.18.1 dev eth1

# Test
# let x be the last digits of current source/target pc
# source: ping -s 1000 -c 4 192.168.x.x -W 1
# target: ping -s 1000 -c 4 192.168.x.x -W 1
