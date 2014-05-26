#!/usr/bin/env ruby

# written: 24 Jan 2013

require 'trollop'
require 'parallel'

opts = Trollop::options do
  banner <<-EOS 

  Script for embarrassingly parallelizing the Popsicle script. Just
  provide the number of threads you want to use!

  Each thread will open a new jvm, max memory allowed is 6gb per
  instance.

  Options:
  EOS
  opt( :threads, "The number of threads to use (won't ckeck value)", 
       type: :int )
end

# set vars
opts[:popsicle] = "/home/moorer/bin/popsicle-0.0.1-10-standalone.jar"
opts[:bam] = "/data/moorer/serc/recruitment/to_illumina/serc_ill_reads_to_serc_ill_contigs.sorted.bam"
opts[:index] = opts[:bam] + ".bai"
opts[:regions] = "/data/moorer/serc/recruitment/to_illumina/reference_names.regions"

opts[:outdir] = "/data/moorer/serc/popsicle/illumina_contigs/bowtie2/all_mapped_contigs"
opts[:stats_out] = opts[:outdir] + "/stats/serc_ill_reads_to_serc_ill_contigs.popsicle10_stats"
opts[:cov_out] = opts[:outdir] + "/stats/serc_ill_reads_to_serc_ill_contigs.popsicle10_cov"
opts[:image_dir] = opts[:outdir] + "/images"


if !File.exist? opts[:popsicle]
  $stderr.puts "The popsicle jar doesn't exist"
  exit
end

if !File.exist? opts[:bam]
  $stderr.puts "The bam file doesn't exist"
  exit
end

if !File.exist? opts[:index]
  $stderr.puts "The index file doesn't exist"
  exit
end

if !File.exist? opts[:regions]
  $stderr.puts "The regions file doesn't exist"
  exit
end

if !File.exist? opts[:outdir]
  $stderr.puts "The outdir doesn't exist"
  exit
end

if opts[:threads] < 2
  $stderr.puts "Run Popsicle directly if you use only 1 thread!"
  exit
end



def parse_fname(fname)
  { dir: File.dirname(fname), 
    base: File.basename(fname, File.extname(fname)), 
    ext: File.extname(fname) }
end

fname = parse_fname opts[:regions]

#### split the region file into subfiles

# to easily reopen
sub_file_names = (0..opts[:threads]-1).to_a
  .map { |n| "#{opts[:outdir]}/#{fname[:base]}_#{n}" }

sub_files = sub_file_names
  .map { |name| File.open( name, 'w' ) }

File.open( opts[:regions], 'r' ).each_with_index do |line, idx|
  sub_files[idx % opts[:threads]].puts line.chomp
end

sub_files.each { |f| f.close }

cmds = Parallel.each_with_index(sub_file_names, 
                               in_processes: opts[:threads]) { |name, idx|
  cmd = "java -jar -Xms256m -Xmx6g " +
  "#{opts[:popsicle]} -b #{opts[:bam]} -i #{opts[:index]} " +
  "-r #{name} -s #{opts[:stats_out]}_#{idx} -d #{opts[:image_dir]} " +
  "-c #{opts[:cov_out]}_#{idx}"

  `#{cmd}`
}

$stderr.puts "base java command"
$stderr.puts( "java -jar -Xms256m -Xmx6g " +
              "#{opts[:popsicle]} -b #{opts[:bam]} -i #{opts[:index]} " +
              "-r #{opts[:regions]} -s #{opts[:stats_out]} " +
              "-d #{opts[:image_dir]} -c #{opts[:cov_out]}" )

$stderr.puts "region file base names...stats and cov files have same idx"
$stderr.puts sub_file_names

sub_files.each { |f| f.delete }
