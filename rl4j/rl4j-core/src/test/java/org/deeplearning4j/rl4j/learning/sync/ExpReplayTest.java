package org.deeplearning4j.rl4j.learning.sync;

import org.deeplearning4j.rl4j.observation.Observation;
import org.deeplearning4j.rl4j.support.MockRandom;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExpReplayTest {
    @Test
    public void when_storingElementWithStorageNotFull_expect_elementStored() {
        // Arrange
        MockRandom randomMock = new MockRandom(null, new int[] { 0 });
        ExpReplay<Integer> sut = new ExpReplay<Integer>(2, 1, randomMock);

        // Act
        Transition<Integer> transition = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                123, 234, false, new Observation(Nd4j.create(1)));
        sut.store(transition);
        List<Transition<Integer>> results = sut.getBatch(1);

        // Assert
        assertEquals(1, results.size());
        assertEquals(123, (int)results.get(0).getAction());
        assertEquals(234, (int)results.get(0).getReward());
    }

    @Test
    public void when_storingElementWithStorageFull_expect_oldestElementReplacedByStored() {
        // Arrange
        MockRandom randomMock = new MockRandom(null, new int[] { 0, 1 });
        ExpReplay<Integer> sut = new ExpReplay<Integer>(2, 1, randomMock);

        // Act
        Transition<Integer> transition1 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                1, 2, false, new Observation(Nd4j.create(1)));
        Transition<Integer> transition2 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                3, 4, false, new Observation(Nd4j.create(1)));
        Transition<Integer> transition3 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                5, 6, false, new Observation(Nd4j.create(1)));
        sut.store(transition1);
        sut.store(transition2);
        sut.store(transition3);
        List<Transition<Integer>> results = sut.getBatch(2);

        // Assert
        assertEquals(2, results.size());

        assertEquals(3, (int)results.get(0).getAction());
        assertEquals(4, (int)results.get(0).getReward());

        assertEquals(5, (int)results.get(1).getAction());
        assertEquals(6, (int)results.get(1).getReward());
    }


    @Test
    public void when_askBatchSizeZeroAndStorageEmpty_expect_emptyBatch() {
        // Arrange
        MockRandom randomMock = new MockRandom(null, new int[] { 0 });
        ExpReplay<Integer> sut = new ExpReplay<Integer>(5, 1, randomMock);

        // Act
        List<Transition<Integer>> results = sut.getBatch(0);

        // Assert
        assertEquals(0, results.size());
    }

    @Test
    public void when_askBatchSizeZeroAndStorageNotEmpty_expect_emptyBatch() {
        // Arrange
        MockRandom randomMock = new MockRandom(null, new int[] { 0 });
        ExpReplay<Integer> sut = new ExpReplay<Integer>(5, 1, randomMock);

        // Act
        Transition<Integer> transition1 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                1, 2, false, new Observation(Nd4j.create(1)));
        Transition<Integer> transition2 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                3, 4, false, new Observation(Nd4j.create(1)));
        Transition<Integer> transition3 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                5, 6, false, new Observation(Nd4j.create(1)));
        sut.store(transition1);
        sut.store(transition2);
        sut.store(transition3);
        List<Transition<Integer>> results = sut.getBatch(0);

        // Assert
        assertEquals(0, results.size());
    }

    @Test
    public void when_askBatchSizeGreaterThanStoredCount_expect_batchWithStoredCountElements() {
        // Arrange
        MockRandom randomMock = new MockRandom(null, new int[] { 0, 1, 2 });
        ExpReplay<Integer> sut = new ExpReplay<Integer>(5, 1, randomMock);

        // Act
        Transition<Integer> transition1 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                1, 2, false, new Observation(Nd4j.create(1)));
        Transition<Integer> transition2 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                3, 4, false, new Observation(Nd4j.create(1)));
        Transition<Integer> transition3 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                5, 6, false, new Observation(Nd4j.create(1)));
        sut.store(transition1);
        sut.store(transition2);
        sut.store(transition3);
        List<Transition<Integer>> results = sut.getBatch(10);

        // Assert
        assertEquals(3, results.size());

        assertEquals(1, (int)results.get(0).getAction());
        assertEquals(2, (int)results.get(0).getReward());

        assertEquals(3, (int)results.get(1).getAction());
        assertEquals(4, (int)results.get(1).getReward());

        assertEquals(5, (int)results.get(2).getAction());
        assertEquals(6, (int)results.get(2).getReward());
    }

    @Test
    public void when_askBatchSizeSmallerThanStoredCount_expect_batchWithAskedElements() {
        // Arrange
        MockRandom randomMock = new MockRandom(null, new int[] { 0, 1, 2, 3, 4 });
        ExpReplay<Integer> sut = new ExpReplay<Integer>(5, 1, randomMock);

        // Act
        Transition<Integer> transition1 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                1, 2, false, new Observation(Nd4j.create(1)));
        Transition<Integer> transition2 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                3, 4, false, new Observation(Nd4j.create(1)));
        Transition<Integer> transition3 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                5, 6, false, new Observation(Nd4j.create(1)));
        Transition<Integer> transition4 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                7, 8, false, new Observation(Nd4j.create(1)));
        Transition<Integer> transition5 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                9, 10, false, new Observation(Nd4j.create(1)));
        sut.store(transition1);
        sut.store(transition2);
        sut.store(transition3);
        sut.store(transition4);
        sut.store(transition5);
        List<Transition<Integer>> results = sut.getBatch(3);

        // Assert
        assertEquals(3, results.size());

        assertEquals(1, (int)results.get(0).getAction());
        assertEquals(2, (int)results.get(0).getReward());

        assertEquals(3, (int)results.get(1).getAction());
        assertEquals(4, (int)results.get(1).getReward());

        assertEquals(5, (int)results.get(2).getAction());
        assertEquals(6, (int)results.get(2).getReward());
    }

    @Test
    public void when_randomGivesDuplicates_expect_noDuplicatesInBatch() {
        // Arrange
        MockRandom randomMock = new MockRandom(null, new int[] { 0, 1, 2, 1, 3, 1, 4 });
        ExpReplay<Integer> sut = new ExpReplay<Integer>(5, 1, randomMock);

        // Act
        Transition<Integer> transition1 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                1, 2, false, new Observation(Nd4j.create(1)));
        Transition<Integer> transition2 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                3, 4, false, new Observation(Nd4j.create(1)));
        Transition<Integer> transition3 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                5, 6, false, new Observation(Nd4j.create(1)));
        Transition<Integer> transition4 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                7, 8, false, new Observation(Nd4j.create(1)));
        Transition<Integer> transition5 = new Transition<Integer>(new Observation(new INDArray[] { Nd4j.create(1) }),
                9, 10, false, new Observation(Nd4j.create(1)));
        sut.store(transition1);
        sut.store(transition2);
        sut.store(transition3);
        sut.store(transition4);
        sut.store(transition5);
        List<Transition<Integer>> results = sut.getBatch(3);

        // Assert
        assertEquals(3, results.size());

        assertEquals(1, (int)results.get(0).getAction());
        assertEquals(2, (int)results.get(0).getReward());

        assertEquals(3, (int)results.get(1).getAction());
        assertEquals(4, (int)results.get(1).getReward());

        assertEquals(5, (int)results.get(2).getAction());
        assertEquals(6, (int)results.get(2).getReward());

    }
}
