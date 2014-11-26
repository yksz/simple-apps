turn = 'o'
grid = new char[3][3]

def show() {
    for (y in 0..<grid.size()) {
        for (x in 0..<grid[0].size())
            print "${grid[y][x]} "
        println ""
    }
}

def reset() {
    turn = 'o'
    for (y in 0..<grid.size())
        for (x in 0..<grid[0].size())
            grid[y][x] = '_'
}

def checkWin(int row, int col) {
    checkRow(row) | checkColumn(col) | checkDiagonal()
}

def checkRow(int row) {
    def base = grid[row][0]
    for (x in 1..<grid[0].size())
        if (base != grid[row][x])
            return false
    return true
}

def checkColumn(int col) {
    def base = grid[0][col]
    for (y in 1..<grid.size())
        if (base != grid[y][col])
            return false
    return true
}

def checkDiagonal() {
    if (grid.size() != grid[0].size())
        return false
    checkSlash() | checkBackSlash()
}

def checkSlash() {
    int size = grid.size()
    def base = grid[0][size - 1]
    if (base == '_')
        return false
    for (i in 1..<size)
        if (base != grid[i][size - 1 - i])
            return false
    return true
}

def checkBackSlash() {
    int size = grid.size()
    def base = grid[0][0]
    if (base == '_')
        return false
    for (i in 1..<size)
        if (base != grid[i][i])
            return false
    return true
}

def checkDraw(wins) {
    if (wins)
        return false
    def draws = true
    for (y in 0..<grid.size())
        for (x in 0..<grid[0].size())
            draws &= grid[y][x] != '_'
    return draws
}

def repl() {
    def reader = System.in.newReader()
    while (true) {
        print "> "
        def line = reader.readLine()
        try {
            eval(line)
        } catch (Throwable e) {
            println e
        }
    }
}

def eval(String line) {
    if (line == null || line.trim().isEmpty())
        return
    def (row, col) = line.split().collect { (it as int) - 1 }
    if (grid[row][col] != '_') {
        println "This position has already marked."
        return
    }
    grid[row][col] = turn
    show()
    def wins = checkWin(row, col)
    if (wins) {
        println "\n=== $turn WINS! ===\n"
        ready()
    } else {
        if (checkDraw(wins)) {
            println "\n=== DRAW ===\n"
            ready()
        } else {
            turn = turn == 'o' ? 'x' : 'o'
            println "Turn: $turn"
        }
    }
}

def ready() {
    println "Start new game..."
    reset()
    println "Turn: $turn"
    show()
}


println "usage: <row> <column> (e.g. 1 2)\n"
ready()
repl()
