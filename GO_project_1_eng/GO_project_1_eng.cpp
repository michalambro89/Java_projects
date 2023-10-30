#include <iostream>
#include <iomanip>

static double product_result;
static double sarrus_result;

struct Point {
    double x;
    double y;
};

double CrossProduct(const Point& A, const Point& B, const Point& P) {
    double vectorAB_x = B.x - A.x;
    double vectorAB_y = B.y - A.y;
    double vectorAP_x = P.x - A.x;
    double vectorAP_y = P.y - A.y;

    product_result = (vectorAB_x * vectorAP_y) - (vectorAB_y * vectorAP_x);
    return product_result;
}

double SarrusMatrix(const Point& A, const Point& B, const Point& P) {
    sarrus_result = (A.x * B.y) + (B.x * P.y) + (P.x * A.y) - (P.x * B.y) - (B.x * A.y) - (A.x * P.y);
    return sarrus_result;
}

double difference() {
    return product_result - sarrus_result;
}

int main() {
    Point A, B, P;

    while (true) {
        std::cout << "Enter the coordinates of point A (x y): ";

        if (std::cin >> A.x >> A.y) {
        }
        else {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            std::cout << "Incorrect data. Please re-enter." << std::endl;
            continue;
        }
        break;
    }

    while (true) {

        std::cout << "Enter the coordinates of point B (x y): ";
        if (std::cin >> B.x >> B.y) {
        }
        else {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            std::cout << "Incorrect data. Please re-enter." << std::endl;
            continue;
        }
        break;
    }

    while (true) {

        std::cout << "Enter the coordinates of point P (x y): ";
        if (std::cin >> P.x >> P.y) {
        }
        else {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            std::cout << "Incorrect data. Please re-enter." << std::endl;
            continue;
        }
        break;
    }

    double crossProduct = CrossProduct(A, B, P);
    double sarrusMatrix = SarrusMatrix(A, B, P);

    std::cout << std::fixed << std::setprecision(6) << "Result of vector product: " << product_result << std::endl;
    std::cout << std::fixed << std::setprecision(6) << "Sarrus method result: " << sarrus_result << std::endl;

    if (crossProduct == 0) {
        std::cout << "Point P lies on the segment AB." << std::endl;
    }
    else if (crossProduct > 0) {
        std::cout << "Punkt P lezy po prawej stronie odcinka AB." << std::endl;
    }
    else {
        std::cout << "Point P lies on the right side of the segment AB." << std::endl;
    }

    std::cout << std::fixed << std::setprecision(6) << "Score difference: " << difference() << std::endl;

    return 0;
}