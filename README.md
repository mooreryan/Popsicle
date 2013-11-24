# Popsicle #

Tasty viewing of bam files!

## Usage ##

java -jar popsicle-0.0.1-standalone.jar \ <br>
-b bam-file.bam -i bam-index.bam.bai -r "reference sequence name"

## Notes ##

- Opens charts in a new java window (make sure X11 forwarding is enabled if running through biohen)
- The coverage is displayed not across the entire length of the reference sequence, but rather only where there is actually coverage

##### Contact #####

Ryan Moore, Graduate Research Assistant <br>
University of Delaware <br>
Center for Bioinformatics and Computational Biology <br>
moorer@udel.edu <br>
