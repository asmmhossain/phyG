#!/usr/bin/env python

"""
Copyright 2012 Oleksandr Moskalenko <om@hpc.ufl.edu>

Runs BEAST on an input XML file.
For use with BEAST version 1.7.1

usage: beast.py inputxml

Produces:
    output log file
    mcmc.operators file
    A variable number of '.tree' files depending on the XML input
"""
import os, shutil, subprocess, sys, optparse, glob, string
from xml.dom.minidom import parse, Node

def stop_err(msg):
    sys.stderr.write("%s\n" % msg)
    sys.exit()

def parseFnames(nodelist):
    filenames = []
    for node in nodelist:
        if node.hasAttributes():
            fname = node.getAttribute('fileName')
            if fname != "":
                filenames.append(fname)
        else:
            pass
    return filenames

def __main__():
    usage = "usage: %prog inputXML"
    parser = optparse.OptionParser(usage = usage)
    parser.add_option("-T", "--threads", action="store", type="string", dest="threads", help="Number of threads")
    parser.add_option("-s", "--seed", action="store", type="string",
            dest="seed", help="Random seed")
    parser.add_option("-r", "--strict", action="store_true", dest="strict", help="Strict XML parsing")
    parser.add_option("-e", "--errors", action="store", type="string",
            dest="errors", help="Maximum number of errors allowed")
    parser.add_option("-i", "--inputxml", action="store", type="string", dest="inputxml", help="Input XML") 
    parser.add_option("-o", "--operators", action="store", type="string", dest="operators", help="Operators")
    parser.add_option("-l", "--logs", action="store", type="string", dest="logs", help="Logs")
    parser.add_option("-t", "--trees", action="store", type="string", dest="trees", help="Trees")
    parser.add_option("-d", "--id", action="store", type="string", dest="treeid", help="Tree ID")
    parser.add_option("-p", "--path", action="store", type="string", dest="path", help="New file path")
    (options, args) = parser.parse_args()
    if options.threads == None:
        threads = 1
    else:
        threads = int(options.threads)
    if options.seed != None:
        seed = int(options.seed)
    else:
        seed = 12345
    if options.strict == "-strict":
        print "Strict XML check was chosen\n"
        strict = True
        print "No strict XML check was chosen\n"
    else:
        strict = False
    inputxml = options.inputxml
    operators = options.operators
    logs = options.logs
    trees = options.trees
    errors = options.errors
    treefile_id = options.treeid
    newfilepath = options.path
    sys.stdout.write("The following parameters have been provided:\n")
    sys.stdout.write("Input XML: %s\n" % inputxml)
    sys.stdout.write("Operators: %s\n" % operators)
    sys.stdout.write("Logs: %s\n" % logs)
    sys.stdout.write("Trees: %s\n" % trees)
    sys.stdout.write("Strict: %s\n" % strict)
    sys.stdout.write("New file path: %s\n" % newfilepath)
    sys.stdout.write("Tree file ID: %s\n" % treefile_id)
    if errors != None:
        sys.stdout.write("Errors: %s\n" % errors)
    cmd = []
    cmd.append('/home/mukarram/Downloads/BEASTv1.8.0/bin/beast') # changing from 'beast' to include the path of beast
    if strict == True:
        cmd.append('-strict')
    thread_opt = "-threads %d" % threads
    sys.stdout.write("Threads: %s\n" % thread_opt)
    cmd.append(thread_opt)
    cmd.append('-seed %d' % int(seed))
    if errors != None:
        cmd.append('-errors %d' % int(errors))
    cmd.append(inputxml)
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
        sys.exit(1)
    else:
        sys.stdout.write(stdout)
        sys.stdout.write(stderr)
#2012-04-24 - 2nd approach, parse the .xml file:
    xml_file = os.path.abspath(inputxml)
    if not os.path.exists(inputxml):
        sys.stderr.write("Cannot find the input XML file for parsing.\n")
    dom = parse(inputxml)
    xml_logs = dom.getElementsByTagName('log')
    xml_trees = dom.getElementsByTagName('logTree')
    logfiles_orig = parseFnames(xml_logs)
    treefiles_orig = parseFnames(xml_trees)
    try:
        if len(logfiles_orig) == 0:
            logfiles_orig = glob.glob("*.log*")
            if len(logfiles_orig) == 0:
                logfiles_orig.append('Error_no_log')
                dummy_file = open('Error_no_log','w')
                dummy_file.write("BEAST run has not produced a log or it's named in such a way that I can't locate it. Configure BEAST to produce .log files without spaces in their names and rerun the analysis.\n")
                dummy_file.close()
        logfiles = []
        if os.path.isdir(newfilepath):
            for filename in logfiles_orig:
                if os.path.isfile(filename):
                    name = string.replace(os.path.splitext(filename)[0], "_", "-")
                    filestring = "primary_%s_%s_visible_nexus" % (treefile_id, name)
                    newpath = os.path.join(newfilepath,filestring)
                    logfiles.append(newpath)
#                else:
#                    sys.stderr.write("Can't find the log file to rename.\n")
        logfiles[0] = logs
        for i in range(len(logfiles_orig)):
            src = logfiles_orig[i]
            dst = logfiles[i]
            if os.path.exists(src):
                shutil.copy(src, dst)
#            else:
#                print "File '%s' can't be found.\n" % src
    except Exception, err:
        sys.stderr.write("Error copying log file: \n%s\n" % err)
    try:
        if not os.path.exists('mcmc.operators'):
            bat = open('mcmc.operators','w')
            bat.write('The mcmc.operators file did not have any output.\n')
            bat.close()
    except Exception, err:
        sys.stderr.write("Error copying mcmc.operators file: \n%s\n" % err)
    try:
        if len(treefiles_orig) == 0:
            print "No tree files found by the xml file parser.\n"
            treefiles_orig = glob.glob("*.trees*")
#                print "Original tree files from the directory:\n\t%s" % " ".join(treefiles_orig)
            if len(treefiles_orig) == 0:
                treefiles_orig.append('Error_no_tree')
                dummy_file = open('Error_no_tree','w')
                dummy_file.write("BEAST run has not produced an output tree or it's named in such a way that I can't locate it. Configure BEAST to produce .tree files without spaces in their names and rerun the analysis.\n")
                dummy_file.close()
        treefiles = []
        if os.path.isdir(newfilepath):
            for filename in treefiles_orig:
                if os.path.isfile(filename):
                    name = string.replace(os.path.splitext(filename)[0], "_", "-")
                    filestring = "primary_%s_%s_visible_nexus" % (treefile_id, name)
                    newpath = os.path.join(newfilepath,filestring)
                    treefiles.append(newpath)
        treefiles[0] = trees
        for i in range(len(treefiles_orig)):
            src = treefiles_orig[i]
            dst = treefiles[i]
            if os.path.exists(src):
                shutil.copy(src, dst)
    except Exception, err:
        sys.stderr.write("Error copying trees file(s): \n%s\n" % err)
if __name__=="__main__": __main__()
