/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  chris
 * Created: Mar 23, 2020
 */

DROP TABLE IF EXISTS Loan;

CREATE TABLE Loan 
(
    loanID INT NOT NULL,
    memberID INT,
    bookID VARCHAR(128),
    borrowDate Date,
    returnDate Date,
    returned BOOLEAN,
    PRIMARY KEY (loanID)
);
