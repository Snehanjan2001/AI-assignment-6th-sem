def isValid(leftSide, rightSide):
    if(leftSide[0] > leftSide[1] or rightSide[0] > rightSide[1]):
        return False
    else:
        return True


def printState(leftSide, rightSide, boat):
    for i in range(0, leftSide[0]):
        print('C', end=" ")
    for i in range(0, leftSide[1]):
        print('M', end=" ")

    if(boat == 'L'):
        print('---------------B---', end=" ")
    else:
        print('---B---------------', end=" ")

    for i in range(0, rightSide[0]):
        print('C', end=" ")
    for i in range(0, rightSide[1]):
        print('M', end=" ")

    print()


states = {}
counter = 0


def dfs(state, boat):
    global counter, states
    # counter += 1
    leftSide, rightSide = state[0], state[1]
    if(rightSide[0] == 0 and rightSide[1] == 0):
        printState(leftSide, rightSide, boat)
        exit(0)
    # if(counter > 100):
    #     exit(0)
    if str(str(state) + boat) in states.keys():
        return
    states[str(str(state) + boat)] = True

    # If boat is in left side of river
    if boat == 'L':
        boat = 'R'
        # 1 cannibal, 1 monk
        if(leftSide[0] >= 1 and leftSide[1] >= 1):
            newRightSide = [rightSide[0] + 1, rightSide[1] + 1]
            newLeftSide = [leftSide[0] - 1, leftSide[1] - 1]
            if(isValid(newLeftSide, newRightSide)):
                printState(leftSide, rightSide, boat)
                dfs([newLeftSide, newRightSide], boat)

        # 2 cannibals
        if(leftSide[0] >= 2):
            newRightSide = [rightSide[0] + 2, rightSide[1]]
            newLeftSide = [leftSide[0] - 2, leftSide[1]]
            if(isValid(newLeftSide, newRightSide)):
                printState(leftSide, rightSide, boat)
                dfs([newLeftSide, newRightSide], boat)

        # 2 Missionaries
        if(leftSide[1] >= 2):
            newRightSide = [rightSide[0], rightSide[1] + 2]
            newLeftSide = [leftSide[0], leftSide[1] - 2]
            if(isValid(newLeftSide, newRightSide)):
                printState(leftSide, rightSide, boat)
                dfs([newLeftSide, newRightSide], boat)

    # If boat is in right side of river
    if boat == 'R':
        boat = 'L'
        # 1 cannibal, 1 monk
        if(rightSide[0] >= 1 and rightSide[1] >= 1):
            newLeftSide = [leftSide[0]+1, leftSide[0]+1]
            newRightSide = [rightSide[0]-1, rightSide[0]-1]
            if(isValid(newLeftSide, newRightSide)):
                printState(leftSide, rightSide, boat)
                dfs([newLeftSide, newRightSide], boat)

        # 2 cannibal
        if(rightSide[0] >= 2):
            newLeftSide = [leftSide[0]+2, leftSide[0]]
            newRightSide = [rightSide[0]-2, rightSide[0]]
            if(isValid(newLeftSide, newRightSide)):
                printState(leftSide, rightSide, boat)
                dfs([newLeftSide, newRightSide], boat)

        # 2 missionary
        if(rightSide[1] >= 2):
            newLeftSide = [leftSide[0], leftSide[0]+2]
            newRightSide = [rightSide[0], rightSide[0]-2]
            if(isValid(newLeftSide, newRightSide)):
                printState(leftSide, rightSide, boat)
                dfs([newLeftSide, newRightSide], boat)


printState([0, 0], [3, 3], 'R')
dfs([[0, 0], [3, 3]], 'R')
