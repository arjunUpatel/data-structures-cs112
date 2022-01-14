import java.util.ArrayList;

public class Transit {
	public static TNode makeList(int[] trainStations, int[] busStops, int[] locations) {
		TNode trainZero = new TNode();
		trainZero.down = new TNode();
		trainZero.down.down = new TNode();
		TNode prevTrainNode = trainZero;
		TNode prevBusNode = trainZero.down;
		TNode prevLocNode = trainZero.down.down;
		for (int i = 0, j = 0, k = 0; k < locations.length; k++) {
			prevLocNode.next = new TNode(locations[k]);
			prevLocNode = prevLocNode.next;
			if (i < trainStations.length && trainStations[i] == locations[k]) {
				prevTrainNode.next = new TNode(locations[k]);
				prevTrainNode = prevTrainNode.next;
				if (j < busStops.length && busStops[j] == locations[k]) {
					prevBusNode.next = new TNode(locations[k]);
					prevBusNode = prevBusNode.next;
					prevTrainNode.down = prevBusNode;
					prevBusNode.down = prevLocNode;
					j++;
				}
				i++;
			}
			if (j < busStops.length && busStops[j] == locations[k]) {
				prevBusNode.next = new TNode(locations[k]);
				prevBusNode = prevBusNode.next;
				prevBusNode.down = prevLocNode;
				j++;
			}
		}
		return trainZero;
	}

	public static void removeTrainStation(TNode trainZero, int station) {
		TNode currentNode = trainZero;
		while (currentNode.next != null && currentNode.next.location < station)
			currentNode = currentNode.next;
		if (currentNode.next != null && currentNode.next.location == station) {
			TNode temp = currentNode.next;
			currentNode.next = currentNode.next.next;
			temp = null;
		}
	}

	public static void addBusStop(TNode trainZero, int busStop) {
		TNode currentBusNode = trainZero.down;
		while (currentBusNode.next != null && currentBusNode.next.location < busStop)
			currentBusNode = currentBusNode.next;
		try {
			if (currentBusNode.next.location != busStop) {
				TNode currentWalkingLoc = currentBusNode.down;
				while (currentWalkingLoc.location < busStop)
					currentWalkingLoc = currentWalkingLoc.next;
				currentBusNode.next = new TNode(busStop, currentBusNode.next, currentWalkingLoc);
			}
		} catch (NullPointerException npe) {
			TNode currentWalkingLoc = currentBusNode.down;
			while (currentWalkingLoc.location < busStop)
				currentWalkingLoc = currentWalkingLoc.next;
			currentBusNode.next = new TNode(busStop, null, currentWalkingLoc);
		}
	}

	public static ArrayList<TNode> bestPath(TNode trainZero, int destination) {
		ArrayList<TNode> locations = new ArrayList<>();
		while (trainZero != null && trainZero.location < destination + 1) {
			locations.add(trainZero);
			try {
				if (trainZero.down != null && destination < trainZero.next.location) {
					trainZero = trainZero.down;
				} else {
					trainZero = trainZero.next;
				}
			} catch (Exception e) {
				trainZero = trainZero.down;
			}
		}
		return locations;
	}

	public static TNode duplicate(TNode trainZero) {
		int[] train, bus, walk;
		int count = 0;
		for (TNode temp = trainZero.next; temp != null; temp = temp.next)
			count++;
		train = new int[count];
		if (count > 0) {
			count = 0;
			for (TNode temp = trainZero.next; temp != null; temp = temp.next) {
				train[count] = temp.location;
				count++;
			}
		}
		count = 0;
		for (TNode temp = trainZero.down.next; temp != null; temp = temp.next)
			count++;
		bus = new int[count];
		if (count > 0) {
			count = 0;
			for (TNode temp = trainZero.down.next; temp != null; temp = temp.next) {
				bus[count] = temp.location;
				count++;
			}
		}
		count = 0;
		for (TNode temp = trainZero.down.down.next; temp != null; temp = temp.next)
			count++;
		walk = new int[count];
		if (count > 0) {
			count = 0;
			for (TNode temp = trainZero.down.down.next; temp != null; temp = temp.next) {
				walk[count] = temp.location;
				count++;
			}
		}
		return makeList(train, bus, walk);
	}

	public static void addScooter(TNode trainZero, int[] scooterStops) {
		TNode walkingLayer = trainZero.down.down;
		TNode busLayer = trainZero.down;
		TNode scoot = new TNode(0, null, trainZero.down.down);
		trainZero.down.down = scoot;
		busLayer = busLayer.next;
		walkingLayer = walkingLayer.next;
		for (int i = 0; i < scooterStops.length && walkingLayer != null; walkingLayer = walkingLayer.next) {
			if (walkingLayer.location == scooterStops[i]) {
				scoot.next = new TNode(scooterStops[i], null, walkingLayer);
				if (busLayer != null && busLayer.location == scooterStops[i]) {
					busLayer.down = scoot.next;
					busLayer = busLayer.next;
				}
				scoot = scoot.next;
				i++;
			}
		}
	}
}