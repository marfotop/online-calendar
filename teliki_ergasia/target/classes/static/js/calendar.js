document.addEventListener("DOMContentLoaded", function() {

// Get the necessary DOM elements
    var goToMonthBtn = document.getElementById("goToMonthBtn");
    var inputMonthYear = document.getElementById("inputMonthYear");

    const toggleSidebarBtn = document.getElementById('toggleSidebarBtn');
    const sidebar = document.getElementById('sidebar');

    toggleSidebarBtn.addEventListener('click', () => {
        if (sidebar.style.display == 'none') {
            sidebar.style.display = 'block';
        }
        else {
            sidebar.style.display = 'none';
        }
    });
// Attach event listener to the "Go to Month" button
    goToMonthBtn.addEventListener("click", function() {
        var inputDate = new Date(inputMonthYear.value); // Get the user-inputted date

        if (!isNaN(inputDate.getTime())) { // Check if the input is a valid date
            currentDate = inputDate; // Set the current date to the user-inputted date
            updateCalendar(); // Update the calendar with the new month
        } else {
            alert("Invalid input! Please enter a valid month in the format MM/YYYY.");
        }
    });


    // Get the necessary DOM elements
    var calendarTable = document.getElementById("calendarTable");
    var calendarBody = document.getElementById("calendarBody");
    var header = document.getElementById("header");
    var prevMonthBtn = document.getElementById("prevMonthBtn");
    var calendarDays = document.getElementById("calendarDays");
    var nextMonthBtn = document.getElementById("nextMonthBtn");
    var headerYear = document.getElementById("headerYear");
    var headerMonth = document.getElementById("headerMonth");
    var addEventBtn = document.getElementById("addEventBtn");
    var weeklyViewBtn = document.getElementById("weeklyViewBtn");
    var monthlyViewBtn = document.getElementById("monthlyViewBtn");
    var yearlyViewBtn = document.getElementById("yearlyViewBtn");


    // Initialize the current date
    var currentDate = new Date();

    // Attach event listeners to the navigation buttons
    prevMonthBtn.addEventListener("click", function() {
        var month = currentDate.getMonth();
        var year = currentDate.getFullYear();
        if (calendarTable.classList.contains("weekly-view")) {
            var currentDay = currentDate.getDate();
            var daysToSubtract = 7; // Go back 7 days for the previous week
            var startingDay = currentDay - 6;
            var realMonthStartingDay = new Date(year, month, 1).getDay(); // first day of month
            if (startingDay < realMonthStartingDay){
                daysToSubtract = realMonthStartingDay - startingDay;
            }
            currentDate.setDate(currentDay - daysToSubtract); // Decrement by the calculated days

            updateCalendar(); // Update the calendar with the new date
        } else if (calendarTable.classList.contains("monthly-view")){
            currentDate.setMonth(currentDate.getMonth() - 1); // Decrement the month for other views
            updateCalendar(); // Update the calendar with the new month
        }
        else{

        }
    });


    nextMonthBtn.addEventListener("click", function() {
        var month = currentDate.getMonth();
        var year = currentDate.getFullYear();
        if (calendarTable.classList.contains("weekly-view")) {
            var currentDay = currentDate.getDate();
            var daysToAdd = 7; // Go forward 7 days for the next week
            var endingDate = currentDay + 6;
            var realMonthEndingDay = new Date(year, month + 1, 0).getDate(); // last day of month
            if (endingDate > realMonthEndingDay){
                daysToAdd = endingDate - realMonthEndingDay;
            }
            currentDate.setDate(currentDay + daysToAdd); // Increment by the calculated days

            updateCalendar(); // Update the calendar with the new date
        } else {
            currentDate.setMonth(currentDate.getMonth() + 1); // Increment the month for other views
            updateCalendar(); // Update the calendar with the new month
        }
    });

    // Attach event listeners to the view buttons
    weeklyViewBtn.addEventListener("click", function() {
        calendarTable.classList.add("weekly-view");
        calendarTable.classList.remove("monthly-view", "yearly-view");
        prevMonthBtn.style.display = "table-row";
        nextMonthBtn.style.display = "table-row";
        headerMonth.style.display = "table-row";

        updateCalendar();
    });

    monthlyViewBtn.addEventListener("click", function() {
        calendarTable.classList.add("monthly-view");
        calendarTable.classList.remove("weekly-view", "yearly-view");
        prevMonthBtn.style.display = "table-row";
        nextMonthBtn.style.display = "table-row";
        headerMonth.style.display = "table-row";
        updateCalendar();
    });

    function generateYearlyCalendar() {
        // Clear the calendar grid
        calendarBody.innerHTML = "";
        document.getElementById("calendarDaysRow").style.display = "none";

        // Generate the calendar grid for the yearly view
        for (var row = 0; row < 4; row++) {
            var newRow = calendarBody.insertRow();

            for (var col = 0; col < 3; col++) {
                var newCell = newRow.insertCell();
                var monthIndex = row * 3 + col; // Calculate the month index based on the row and column

                // Create a table element for the monthly calendar
                var monthlyTable = document.createElement("table");
                monthlyTable.classList.add("monthly-calendar");

                // Generate the calendar grid for the month
                generateMonthlyCalendar(monthlyTable, monthIndex);

                // Append the monthly table to the cell
                newCell.appendChild(monthlyTable);
            }
        }
    }
    yearlyViewBtn.addEventListener("click", function() {
        calendarTable.classList.remove("weekly-view");
        prevMonthBtn.style.display = "none";
        nextMonthBtn.style.display = "none";
        headerMonth.style.display = "none";
        generateYearlyCalendar();
    });

// Function to generate the calendar grid for a specific month
    function generateMonthlyCalendar(table, monthIndex) {
        var month = monthIndex;
        var year = currentDate.getFullYear();
        var th = document.createElement('th');
        var text = document.createTextNode(getMonthName(month)); //cell
        th.appendChild(text);
        table.appendChild(th);
        // Get the starting day and total days for the month
        var startingDay = new Date(year, month, 1).getDay();
        var totalDays = new Date(year, month + 1, 0).getDate();

        var date = 1;
        for (var row = 0; row < 6; row++) {
            var newRow = table.insertRow();

            for (var col = 0; col < 7; col++) {
                if (row === 0 && col < startingDay) {
                    // Empty cells before the first day of the month
                    var newCell = newRow.insertCell();
                    newCell.classList.add("empty");
                } else if (date > totalDays) {
                    // Empty cells after the last day of the month
                    var newCell = newRow.insertCell();
                    newCell.classList.add("empty");
                } else {
                    // Cells for each day of the month
                    var newCell = newRow.insertCell();
                    newCell.textContent = date;
                    newCell.addEventListener("click", selectDate);

                    // Highlight the current day
                    if (
                        date === new Date().getDate() &&
                        month === new Date().getMonth() &&
                        year === new Date().getFullYear()
                    ) {
                        newCell.classList.add("today");
                    }
                    date++;
                }
            }
        }
    }


    // Generate the initial calendar
    updateCalendar();

    // Function to update the calendar based on the current date and view
    function updateCalendar() {
        var month = currentDate.getMonth();
        var year = currentDate.getFullYear();

        // Update the month and year display
        headerMonth.textContent = getMonthName(month);
        headerYear.textContent = year;

        // Clear the calendar grid
        calendarBody.innerHTML = "";

        if (calendarTable.classList.contains("weekly-view")) {
            // Generate the calendar grid for the weekly view
            document.getElementById("calendarDaysRow").style.display = "table-row";

            var startingDay = currentDate.getDate() - currentDate.getDay();
            var intStartingDay = currentDate.getDay();
            var endingDay = startingDay + 6;
            var intEndingDay = 6;
            var realMonthStartingDay = new Date(year, month, 1).getDay(); // first day of month
            var realMonthEndingDay = new Date(year, month + 1, 0).getDate();
            for (var row = 0; row < 1; row++) {
                var cellIndex = 0;
                var newRow = calendarBody.insertRow();
                if (startingDay <= 0) {
                    intStartingDay = currentDate.getDay() - currentDate.getDate() + 1;
                    startingDay = new Date(year, month - 1, 0).getDate() - intStartingDay + 1;
                }
                if (startingDay >= 25) {
                    endingDay = realMonthEndingDay;
                }
                if (endingDay > realMonthEndingDay) {
                    endingDay = realMonthEndingDay;
                }

                var listOfCells = [];
                var i = 0;
                for (let col = startingDay; col <= endingDay; col++) {
                    var newCell = newRow.insertCell();
                    listOfCells.push(newCell);
                    newCell.textContent = col;
                    newCell.addEventListener("click", selectDate);
                    // Highlight the current day
                    if (
                        col === new Date().getDate() &&
                        month === new Date().getMonth() &&
                        year === new Date().getFullYear()
                    ) {
                        newCell.classList.add("today");
                    }
                    i++
                }
                if(i<7){
                    var j = 1;
                    for(i;i<7;i++){
                        var newCell = newRow.insertCell();
                        listOfCells.push(newCell);
                        newCell.textContent = j;
                        newCell.addEventListener("click", selectDate);
                        // Highlight the current day
                        if (
                            col === new Date().getDate() &&
                            month === new Date().getMonth() &&
                            year === new Date().getFullYear()
                        ) {
                            newCell.classList.add("today");
                        }
                        j++
                    }
                }

            }
        }

        else if (calendarTable.classList.contains("yearly-view")) {

        }

        else {
            // Generate the calendar grid for the monthly view
            document.getElementById("calendarDaysRow").style.display = "table-row";

            var startingDay = new Date(year, month, 1).getDay();
            var totalDays = new Date(year, month + 1, 0).getDate(); // represents the last day of the month (0 is last day of the desired month)

            var date = 1;
            for (var row = 0; row < 6; row++) {
                var newRow = calendarBody.insertRow();

                for (var col = 0; col < 7; col++) {
                    if (row === 0 && col < startingDay) {
                        // Empty cells before the first day of the month

                        var newCell = newRow.insertCell();
                        newCell.classList.add("empty");
                    } else if (date > totalDays) {
                        // Empty cells after the last day of the month
                        var newCell = newRow.insertCell();
                        newCell.classList.add("empty");
                    } else {
                        // Cells for each day of the month
                        var newCell = newRow.insertCell();
                        newCell.textContent = date;
                        newCell.addEventListener("click", selectDate);


                        // Highlight the current day
                        if (
                            date === new Date().getDate() &&
                            month === new Date().getMonth() &&
                            year === new Date().getFullYear()
                        ) {
                            newCell.classList.add("today");
                        }
                        date++;
                    }
                }
            }
        }
    }
});


// Function to handle selecting a date
function selectDate(event) {
    var selectedCell = event.target;

    // Remove the selected class from any previously selected cell
    var selectedCells = calendarTable.getElementsByClassName("selected");
    for (var i = 0; i < selectedCells.length; i++) {
        selectedCells[i].classList.remove("selected");
    }

    // Add the selected class to the clicked cell
    selectedCell.classList.add("selected");
}

// Function to get the name of the month
function getMonthName(month) {
    var months = [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    ];

    return months[month];
}

// Get the necessary DOM elements
var goToCurrentMonthBtn = document.getElementById("goToCurrentMonthBtn");

//  Attach event listener to the button
goToCurrentMonthBtn.addEventListener("click", function() {
    var today = new Date();
    var currentMonth = today.getMonth() + 1; // Months are zero-based
    var currentYear = today.getFullYear();

    // Redirect to the current month
    var url = "calendar.html?month=" + currentMonth + "&year=" + currentYear;
    window.location.href = url;
});

// Get the necessary DOM elements
var calendarTable = document.getElementById("calendarTable");

// Attach event listener to the calendar table
calendarTable.addEventListener("click", function(event) {
    var target = event.target;

    // Check if a date cell is clicked
    if (target.tagName === "TD") {
        // Get the date value from the clicked cell
        var date = target.dataset.date;

        // Redirect to the new event page with the selected date
        var url = "new-event.html?date=" + date;
        window.location.href = url;
    }
});