
## std. dev. in cov 
boxplot(z$sd, xlab='Standard deviation', main='Boxplot')
boxplot(z$sd, log='y', xlab='Standard deviation', main='Boxplot (log)')
hist(z$sd, xlab='Standard deviation', main='Histogram')

summary(z$sd)
##  Min.  1st Qu.   Median     Mean  3rd Qu.     Max. 
## 1.065   34.970   80.470  183.400  173.600 7190.000 

sd(z$sd)
## [1] 438.6999

## mean / median
2.27911


## mean coverage info
boxplot(z$mean, xlab='Mean', main='Boxplot')
boxplot(z$mean, log='y', xlab='Mean', main='Boxplot (log)')
hist(z$mean, xlab='Mean', main='Histogram')

summary(z$mean)
##  Min.   1st Qu.    Median      Mean   3rd Qu.      Max. 
## 1.135    95.710   182.900   314.400   331.900 14080.000 

sd(z$mean)
## [1] 610.9612

## mean / median
1.718972



## std dev / mean coverage 
boxplot(z$sd.mean, xlab='Std. dev / mean', main='Boxplot')
boxplot(z$sd.mean, log='y', xlab='Std. dev / mean', main='Boxplot (log)')
hist(z$sd.mean, xlab='Std. dev / mean', main='Histogram')

summary(z$sd.mean)
##   Min. 1st Qu.  Median    Mean 3rd Qu.    Max. 
## 0.1350  0.3141  0.4483  0.4989  0.6227  3.3270 

sd(z$sd.mean)
## [1] 0.2499521

## mean / median
1.112871



## std dev / range coverage 
boxplot(z$sd.range, xlab='Std. dev / range', main='Boxplot')
boxplot(z$sd.range, log='y', xlab='Std. dev / range', main='Boxplot (log)')
hist(z$sd.range, xlab='Std. dev / range', main='Histogram')

summary(z$sd.range)
## Min. 1st Qu.  Median    Mean 3rd Qu.    Max. 
## 0.08609 0.18980 0.22370 0.22590 0.25860 0.38870 

sd(z$sd.range)
## 0.04890137

## mean / median
1.009835



## peak coverage 
boxplot(z$peak, xlab='Peak', main='Boxplot')
boxplot(z$peak, log='y', xlab='Peak', main='Boxplot (log)')
hist(z$peak, xlab='Peak', main='Histogram')

summary(z$peak)
##   Min. 1st Qu.  Median    Mean 3rd Qu.    Max. 
## 0.1970  0.4343  0.5018  0.5111  0.5813  0.9441 

sd(z$peak)
## [1] 0.109554

## mean / median
1.018533



## sign.change coverage 
boxplot(z$sign.change, xlab='Sign change', main='Boxplot')
boxplot(z$sign.change, log='y', xlab='Sign change', main='Boxplot (log)')
hist(z$sign.change, xlab='Sign change', main='Histogram')

summary(z$sign.change)
##     Min.  1st Qu.   Median     Mean  3rd Qu.     Max. 
## 0.000250 0.002929 0.005984 0.008459 0.012150 0.048100 

sd(z$sign.change)
## [1] 0.00722754

## mean / median
1.413603



# greater than 1 sd above mean peak and mean sign change
write.csv(subset(z, z$sign.change > (0.008459 + 0.00722754) & z$peak > (0.5111 + 0.109554)),
          file='/Users/ryanmoore/Desktop/serc/popsicle_out/greater_one_std_dev_peak_and_sign_change.csv')

# greater than 1 sd above mean peak and less than 1 sd below mean sign change
write.csv(subset(z, z$sign.change > (0.008459 + 0.00722754) & z$peak < (0.5111 - 0.109554)),
          file='/Users/ryanmoore/Desktop/serc/popsicle_out/less_one_std_dev_peak_and_greater_one_std_dev_sign_change.csv')

# less than 1 sd below mean peak and less than 1 sd below mean sign change
write.csv(subset(z, z$sign.change < (0.008459 - 0.00722754) & z$peak < (0.5111 - 0.109554)),
          file='/Users/ryanmoore/Desktop/serc/popsicle_out/less_one_std_dev_peak_and_sign_change.csv')

# greater than 1 sd above mean peak and less than 1 sd below mean sign change
write.csv(subset(z, z$sign.change < (0.008459 - 0.00722754) & z$peak > (0.5111 + 0.109554)),
          file='/Users/ryanmoore/Desktop/serc/popsicle_out/greater_one_std_dev_peak_and_less_one_std_dev_sign_change.csv')

# greater than 1 sd peak
write.csv(subset(z, z$peak > (0.5111 + 0.109554)),
          file='/Users/ryanmoore/Desktop/serc/popsicle_out/greater_one_std_dev_peak.csv')
