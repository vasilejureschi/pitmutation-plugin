package org.jenkinsci.plugins.pitmutation.target;

import org.jenkinsci.plugins.pitmutation.targets.*;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Ed Kimber
 */
public class MetricTest {

  @Test
  public void testIntMetric() {
    assertThat(MutationMetric.UNDETECTED.createMetric(7).getValue(), is(7));

    assertThat(MutationMetric.UNDETECTED.createMetric(4)
            .aggregate(
                    MutationMetric.UNDETECTED.createMetric(7))
            .getValue(), is(11));

    assertThat(MutationMetric.UNDETECTED.createMetric(4)
            .delta(
                    MutationMetric.UNDETECTED.createMetric(7))
            .getValue(), is(-3));
  }

  @Test
  public void ratioMetricTest() {
    Metric<Integer> undetected = MutationMetric.UNDETECTED.createMetric(3);
    Metric<Integer> total = MutationMetric.TOTAL_MUTATIONS.createMetric(7);

    IntPercentMetric killRatio = MutationMetric.KILL_RATIO.createMetric(new IntPercentage(undetected, total));

    assertThat(killRatio.getValue().getPercentage(), is(3f/7f * 100f));

    IntPercentMetric nextKillRatio = MutationMetric.KILL_RATIO.createMetric(new IntPercentage(
            MutationMetric.UNDETECTED.createMetric(7),
            MutationMetric.TOTAL_MUTATIONS.createMetric(11)
            ));

    assertThat(killRatio.aggregate(nextKillRatio).getValue().getPercentage(), is(10f/18f * 100f));
    assertThat(killRatio.delta(nextKillRatio).getValue().getPercentage(), is((7f-3f)/(11f-7f) * 100f));
  }


}
