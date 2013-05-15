#!/usr/bin/python

import re
import fileinput
import pprint
import StringIO
from pychart import *

def main():

    # Get data
    series = {}
    counts = { "10k_4M": 3899674,
               #"34k_13M": 13233491,
               "68k_26M": 26464524,
             }
    raw_data = ""
    for line in fileinput.input():
        expr_str = r'odin_76_canonical_(.+)\.zip - Day (.+) run took (.+) seconds, starting at ([^\s]+) and .*CompletedSuccessfully'
        expr = re.compile(expr_str)
        mres = re.search(expr, line)
        if mres is None:
            continue
        raw_data += line
        [ dataset, mode, secs, start ] = mres.groups()
        rps = counts[dataset] / int(secs)
        
        skey = (dataset, mode)
        if not series.has_key(skey):
            series[skey] = []
        series[skey].append((start, rps))

    # Draw chart

    # Get X axis dates
    all_dates = {}
    for pair, points in series.items():
        for p in points:
            all_dates[p[0]] = None
    sorted_dates = all_dates.keys()
    sorted_dates.sort()
    date2idx = {}
    i = 1
    for d in sorted_dates:
        date2idx[d] = i
        i += 1
    first_date = sorted_dates[0]
    last_date = sorted_dates[-1]
    date_summary = "Run No. of " + str(len(sorted_dates)) + " runs, "  + first_date + " -- " + last_date
    
    # Prepare chart area
    xaxis = axis.X(label=date_summary, format="%d")
    yaxis = axis.Y(label="Records//sec", tic_interval=250, format="%d")
    ar = area.T(loc=(-200, -200), size=(400,400), x_axis = xaxis, y_axis = yaxis, y_range=(0,2000))

    for pair, points in sorted(series.items()):
        name = " Day".join(pair)
        points = map(lambda p: (date2idx[p[0]], p[1]), points)
        ar.add_plot(line_plot.T(label=name, data=points))

    # Get in-memory "canvas file" so we can gobble up the PDF code in a string
    string_file = StringIO.StringIO()
    string_canvas = canvas.init(fname=string_file, format="pdf")

    # Draw the plot and get the PDF
    ar.draw(string_canvas)
    string_canvas.close()
    pdf_string = string_file.getvalue()

    pdf_fn = "IngestionPerformanceDaily.pdf"
    with open(pdf_fn, "wb") as pdf_output_file:
        pdf_output_file.write(pdf_string)

    raw_data_fn = "raw_data.txt"
    with open(raw_data_fn, "w") as raw_output_file:
        raw_output_file.write(raw_data)

    print "\nAttached are results of performance runs done from:\n\t" + first_date + "\nthrough\n\t" + last_date + "\n"
    print "The PDF chart is in file '" + pdf_fn + "' and raw data is in '" + raw_data_fn + "'"

# Pretty print arg
def pp(x):
    print pprint.PrettyPrinter().pformat(x)
        
# Do it
main()
