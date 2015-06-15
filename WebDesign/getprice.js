function getPrice() {
	var Apples = document.getElementById("Apples").value;
	var Oranges = document.getElementById("Oranges").value;
	var Bananas = document.getElementById("Bananas").value;

	document.getElementById("cost").value = totalCost = 
	(Apples*0.59 + Oranges*0.49 + Bananas*0.39) * 1.05;

	alert("your total cost is $" + totalCost);
}
