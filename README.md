# Popsicle #

Tasty viewing of bam files!

## Overview ##

Given a sorted bam file, an index file and a reference sequnce, Popsicle gives a histogram of read coverage for the given reference.

## Usage ##

java -jar popsicle-0.0.1-standalone.jar \ <br>
-b sorted-bam-file.bam -i bam-index.bam.bai -r "reference sequence name"

## Notes ##

- The bam file must be sorted. Make your index from this sorted file.
- Only enter one reference sequence name at a time.
- Opens charts in a new java window (make sure X11 forwarding is enabled if running through biohen)
- The coverage is displayed not across the entire length of the reference sequence, but rather only where there is actually coverage

##### Contact #####

Ryan Moore, Graduate Research Assistant <br>
University of Delaware <br>
Center for Bioinformatics and Computational Biology <br>
moorer@udel.edu <br>
