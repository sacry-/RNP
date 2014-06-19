# sudo /sbin/rcSuSEfirewall2 restart

sudo /usr/sbin/iptables -F

sudo /usr/sbin/iptables -P INPUT ACCEPT
sudo /usr/sbin/iptables -P OUTPUT ACCEPT
sudo /usr/sbin/iptables -P FORWARD ACCEPT

sudo /usr/sbin/iptables -A INPUT -s localhost -j ACCEPT
sudo /usr/sbin/iptables -A OUTPUT -d localhost -j ACCEPT

sudo /usr/sbin/iptables -A INPUT -s 141.22.192.100 -j ACCEPT
sudo /usr/sbin/iptables -A OUTPUT -d 141.22.192.100 -j ACCEPT

sudo /usr/sbin/iptables -A INPUT -s 141.22.27.107 -j ACCEPT
sudo /usr/sbin/iptables -A OUTPUT -d 141.22.27.107 -j ACCEPT

sudo /usr/sbin/iptables -A INPUT -s cifs.informatik.haw-hamburg.de -j ACCEPT
sudo /usr/sbin/iptables -A OUTPUT -d cifs.informatik.haw-hamburg.de -j ACCEPT

