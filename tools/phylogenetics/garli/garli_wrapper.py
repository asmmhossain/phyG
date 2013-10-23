#!/usr/bin/env python

"""
Runs Garli-2.0 on a sequence file.
For use with Garli version 2.0

usage: garli_wrapper.py config_file best_tree all_trees run_log screen_log out_conf
"""
import os, shutil, subprocess, sys

def stop_err(msg):
    sys.stderr.write("%s\n" % msg)
    sys.exit()

def __main__():
    usage = "usage: %prog input conf btree balltree runlog scrlog outconf"
    conf = sys.argv[1]
    balltree = 'garli.best.all.tre'
    btree = 'garli.best.tre'
    runlog = 'garli.log00.log'
    scrlog = 'garli.screen.log'
    cmd = []
    cmd.append('Garli-2.0')
    cmd.append(os.path.basename(conf))
    try:
        proc = subprocess.Popen(args=cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    except Exception, err:
        sys.stderr.write("Error invoking command: \n%s\n\n%s\n" % (cmd, err))
        sys.exit(1)
    stdout, stderr = proc.communicate()
    return_code = proc.returncode
    if return_code:
        sys.stdout.write(stdout)
        sys.stderr.write(stderr)
        sys.stderr.write("Return error code %i from command:\n" % return_code)
        sys.stderr.write("%s\n" % cmd)
    else:
        sys.stdout.write(stdout)
        sys.stdout.write(stderr)
    try:
        shutil.copyfile(os.path.basename(conf), 'garli.conf')
        if not os.path.exists(balltree):
            bat = open(balltree,'w')
            bat.write('Only a single tree was found, so this file does not have any output')
            bat.close()
    except Exception, err:
        sys.stderr.write("Error copying output files: \n%s\n" % err)
if __name__=="__main__": __main__()
