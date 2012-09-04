#!/usr/bin/python
import sys
import json

def make_curl_request(longd, latd, station_name):
    base_addr = 'http://localhost:9200/geo_metro/station/'
    base_request = 'curl -XPOST %s' % base_addr

    args = {}
    args['name'] = station_name
    args['location'] = {'lat' : latd, 'lon' : longd}
    args_string = json.dumps(args).replace("'", " ")
    args_string = args_string.replace(')', '').replace('(', '')

    return base_request + " -d '%s'" % args_string

def main(filepath):
    for line in open(filepath,'r'):
        comp = line.split('#')
        latd = comp[1]
        longd = comp[2]
        station_name = comp[3]
        print make_curl_request(longd,latd, station_name)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print "Argument errror.\nUsage: python %s stations_coords.csv" % sys.argv[0]
        sys.exit(1)
    else:
        main(sys.argv[1])
